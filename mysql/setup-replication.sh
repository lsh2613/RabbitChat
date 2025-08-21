#!/bin/bash

# MySQL Master-Replica 복제 설정 자동화 스크립트
set -e

echo "MySQL Master-Replica 복제 설정을 시작합니다..."

# Master DB가 완전히 시작될 때까지 대기
echo "Master DB 연결 대기 중..."
until mysql -h rabbit-mysql-master -u root -p1234 -e "SELECT 1" > /dev/null 2>&1; do
    echo "Master DB 연결 대기 중..."
    sleep 3
done

# Replica DB가 완전히 시작될 때까지 대기
echo "Replica DB 연결 대기 중..."
until mysql -h rabbit-mysql-replica -u root -p1234 -e "SELECT 1" > /dev/null 2>&1; do
    echo "Replica DB 연결 대기 중..."
    sleep 3
done

# 추가 안정화 대기 시간
echo "DB 초기화 완료 대기 중..."
sleep 10

# Master DB에서 복제 사용자가 생성되었는지 확인
echo "Master DB에서 복제 사용자 확인 중..."
REPLICA_USER_EXISTS=$(mysql -h rabbit-mysql-master -u root -p1234 -e "SELECT COUNT(*) FROM mysql.user WHERE user='replica';" 2>/dev/null | tail -n 1)
if [ "$REPLICA_USER_EXISTS" -eq 0 ]; then
    echo "복제 사용자가 존재하지 않습니다. 생성 중..."
    mysql -h rabbit-mysql-master -u root -p1234 << EOF
CREATE USER IF NOT EXISTS 'replica'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
GRANT REPLICATION SLAVE ON *.* TO 'replica'@'%';
FLUSH PRIVILEGES;
EOF
    echo "복제 사용자가 생성되었습니다."
fi

# Master DB에서 바이너리 로그 상태 확인
echo "Master DB에서 바이너리 로그 상태 확인 중..."
MASTER_STATUS=$(mysql -h rabbit-mysql-master -u root -p1234 -e "SHOW MASTER STATUS\G" 2>/dev/null)
MASTER_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
MASTER_POSITION=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')

echo "Master File: $MASTER_FILE"
echo "Master Position: $MASTER_POSITION"

if [ -z "$MASTER_FILE" ] || [ -z "$MASTER_POSITION" ]; then
    echo "❌ Master 상태를 가져올 수 없습니다."
    exit 1
fi

# 기존 복제 설정 정리
echo "기존 복제 설정 정리 중..."
mysql -h rabbit-mysql-replica -u root -p1234 << EOF
STOP SLAVE;
RESET SLAVE ALL;
EOF

# Replica DB에서 Master 설정
echo "Replica DB에서 Master 연결 설정 중..."
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

# 복제 연결 대기 및 상태 확인
echo "복제 연결 대기 중..."
for i in {1..30}; do
    SLAVE_STATUS=$(mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" 2>/dev/null)
    IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
    SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')

    echo "시도 $i/30 - IO Running: $IO_RUNNING, SQL Running: $SQL_RUNNING"

    if [ "$IO_RUNNING" = "Yes" ] && [ "$SQL_RUNNING" = "Yes" ]; then
        echo "✅ MySQL Master-Replica 복제 설정이 성공적으로 완료되었습니다!"

        # 복제 상태 상세 정보 출력
        echo "=== 복제 상태 상세 정보 ==="
        mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" | grep -E "(Slave_IO_Running|Slave_SQL_Running|Master_Host|Master_User|Read_Master_Log_Pos|Exec_Master_Log_Pos)"
        exit 0
    elif [ "$IO_RUNNING" = "No" ]; then
        echo "❌ IO 스레드 연결 실패. 오류 확인 중..."
        LAST_IO_ERROR=$(echo "$SLAVE_STATUS" | grep "Last_IO_Error:" | cut -d':' -f2- | xargs)
        if [ -n "$LAST_IO_ERROR" ]; then
            echo "IO 오류: $LAST_IO_ERROR"
        fi
        break
    fi

    sleep 2
done

echo "❌ 복제 설정에 문제가 발생했습니다."
echo "최종 상태: IO Running: $IO_RUNNING, SQL Running: $SQL_RUNNING"

# 오류 정보 출력
echo "=== 복제 오류 정보 ==="
mysql -h rabbit-mysql-replica -u root -p1234 -e "SHOW SLAVE STATUS\G" | grep -E "(Last_IO_Error|Last_SQL_Error)"

exit 1
