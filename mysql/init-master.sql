-- Master DB 초기화 스크립트
-- 복제용 사용자 생성
CREATE USER 'replica'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
GRANT REPLICATION SLAVE ON *.* TO 'replica'@'%';
FLUSH PRIVILEGES;
