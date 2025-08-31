#!/bin/bash

# MySQL Master-Replica 복제 설정 자동화 스크립트
set -e

log() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') [+] $1"
}
sublog() {
  echo "$(date '+%Y-%m-%d %H:%M:%S')    ⠿ $1"
}
suberr() {
  echo "$(date '+%Y-%m-%d %H:%M:%S')    ❌ $1" >&2
}

log "MySQL Master-Replica 복제 설정을 시작합니다..."

log "Master DB 연결 대기 중..."
until mysql -h rabbit-mysql-master -u root -p1234 -e "SELECT 1" > /dev/null 2>&1; do
  sublog "Master DB 연결 대기 중..."
  sleep 3
done
sublog "Master DB 연결됨"

log "Replica DB 연결 대기 중..."
until mysql -h rabbit-mysql-replica -u root -p1234 -e "SELECT 1" > /dev/null 2>&1; do
  sublog "Replica DB 연결 대기 중..."
  sleep 3
done
sublog "Replica DB 연결됨"

log "DB 초기화 완료 대기 중..."
sleep 10
sublog "DB 초기화 완료"

log "Master DB에서 복제 사용자 확인 중..."
REPLICA_USER_EXISTS=$(mysql -h rabbit-mysql-master -u root -p1234 -e "SELECT COUNT(*) FROM mysql.user WHERE user='replica';" 2>/dev/null | tail -n 1)
if [ "$REPLICA_USER_EXISTS" -eq 0 ]; then
  sublog "복제 사용자가 존재하지 않습니다. 생성 중..."
  mysql -h rabbit-mysql-master -u root -p1234 << EOF
CREATE USER IF NOT EXISTS 'replica'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
GRANT REPLICATION SLAVE ON *.* TO 'replica'@'%';
FLUSH PRIVILEGES;
EOF
  sublog "복제 사용자가 생성되었습니다."
else
  sublog "복제 사용자가 이미 존재합니다."
fi

log "Master DB에서 바이너리 로그 상태 확인 중..."
MASTER_STATUS=$(mysql -h rabbit-mysql-master -u root -p1234 -e "SHOW MASTER STATUS\G" 2>/dev/null)
MASTER_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
MASTER_POSITION=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')
sublog "Master File: $MASTER_FILE"
sublog "Master Position: $MASTER_POSITION"

if [ -z "$MASTER_FILE" ] || [ -z "$MASTER_POSITION" ]; then
  suberr "Master 상태를 가져올 수 없습니다."
  exit 1
fi

log "기존 복제 설정 정리 중..."
mysql -h rabbit-mysql-replica -u root -p1234 << EOF
STOP SLAVE;
RESET SLAVE ALL;
EOF
sublog "기존 복제 설정 정리 완료"

log "Replica DB에서 Master 연결 설정 중..."
mysql -h rabbit-mysql-replica -u root -p1234 << EOF
CHANGE MASTER TO
    MASTER_HOST='rabbit-mysql-master',
    MASTER_USER='replica',
    MASTER_PASSWORD='1234',
    MASTER_LOG_FILE='$MASTER_FILE',
    MASTER_LOG_POS=$MASTER_POSITION,
    MASTER_CONNECT_RETRY=10,
    MASTER_RETRY_COUNT=3;
START SLAVE;
EOF
sublog "Master 연결 설정 완료"

log "복제 연결 대기 및 상태 확인 중..."
for i in {1..30}; do
  SLAVE_STATUS=$(mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" 2>/dev/null)
  IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
  SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')

  sublog "시도 $i/30 - IO Running: $IO_RUNNING, SQL Running: $SQL_RUNNING"

  if [ "$IO_RUNNING" = "Yes" ] && [ "$SQL_RUNNING" = "Yes" ]; then
    sublog "✅   MySQL Master-Replica 복제 설정이 성공적으로 완료되었습니다!"
    log "복제 상태 상세 정보 출력"
    mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" | grep -E "(Slave_IO_Running|Slave_SQL_Running|Master_Host|Master_User|Read_Master_Log_Pos|Exec_Master_Log_Pos)"
    exit 0
  elif [ "$IO_RUNNING" = "No" ]; then
    suberr "IO 스레드 연결 실패. 오류 확인 중..."
    LAST_IO_ERROR=$(echo "$SLAVE_STATUS" | grep "Last_IO_Error:" | cut -d':' -f2- | xargs)
    if [ -n "$LAST_IO_ERROR" ]; then
      suberr "IO 오류: $LAST_IO_ERROR"
    fi
    break
  fi
  sleep 2
done

suberr "복제 설정에 문제가 발생했습니다. 최종 상태: IO Running: $IO_RUNNING, SQL Running: $SQL_RUNNING"
log "복제 오류 정보 출력"
mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" | grep -E "(Last_IO_Error|Last_SQL_Error)"
exit 1
