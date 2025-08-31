-- 외래 키 체크 비활성화 (데이터 삭제 및 삽입 시 제약 조건 무시)
SET
FOREIGN_KEY_CHECKS = 0;

-- 기존 데이터 삭제
DELETE
FROM `user`;
DELETE
FROM `chat_room`;
DELETE
FROM `chat_room_member`;
DELETE
FROM `chat_message`;
DELETE
FROM `oauth`;
DELETE
FROM `chat_message_status`;

-- AUTO_INCREMENT 값 초기화
ALTER TABLE `user` AUTO_INCREMENT = 1;
ALTER TABLE `chat_room` AUTO_INCREMENT = 1;
ALTER TABLE `chat_room_member` AUTO_INCREMENT = 1;
ALTER TABLE `chat_message` AUTO_INCREMENT = 1;
ALTER TABLE `oauth` AUTO_INCREMENT = 1;
ALTER TABLE `chat_message_status` AUTO_INCREMENT = 1;

-- 빠른 삽입을 위해 검사 비활성화
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- 테이블 `user` 데이터 덤핑
--
LOCK
TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `username`, `password`, `nickname`, `role`, `created_at`, `updated_at`)
VALUES (1, 'user_1', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_1', 'USER', NOW(), NOW()),
       (2, 'user_2', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_2', 'USER', NOW(), NOW()),
       (3, 'user_3', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_3', 'USER', NOW(), NOW()),
       (4, 'user_4', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_4', 'USER', NOW(), NOW()),
       (5, 'user_5', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_5', 'USER', NOW(), NOW()),
       (6, 'user_6', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_6', 'USER', NOW(), NOW()),
       (7, 'user_7', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_7', 'USER', NOW(), NOW()),
       (8, 'user_8', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_8', 'USER', NOW(), NOW()),
       (9, 'user_9', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_9', 'USER', NOW(), NOW()),
       (10, 'user_10', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', 'USER_10', 'USER', NOW(), NOW());
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
INSERT INTO `chat_room` (`chat_room_id`, `title`, `max_capacity`, `created_at`, `updated_at`)
VALUES (1, 'CHAT_ROOM_1', 10, NOW(), NOW()),
       (2, 'CHAT_ROOM_2', 10, NOW(), NOW()),
       (3, 'CHAT_ROOM_3', 10, NOW(), NOW()),
       (4, 'CHAT_ROOM_4', 10, NOW(), NOW()),
       (5, 'CHAT_ROOM_5', 10, NOW(), NOW()),
       (6, 'CHAT_ROOM_6', 10, NOW(), NOW())
;

/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_room_member` WRITE;
/*!40000 ALTER TABLE `chat_room_member` DISABLE KEYS */;
INSERT INTO `chat_room_member` (`chat_room_member_id`, `chat_room_id`, `user_id`, `role`, `created_at`, `updated_at`)
VALUES (1, 1, 1, 'ADMIN', NOW(), NOW()),
       (2, 1, 2, 'MEMBER', NOW(), NOW()),
       (3, 2, 1, 'MEMBER', NOW(), NOW()),
       (4, 2, 2, 'ADMIN', NOW(), NOW()),
       (5, 3, 1, 'MEMBER', NOW(), NOW()),
       (6, 3, 2, 'MEMBER', NOW(), NOW()),
       (7, 3, 3, 'ADMIN', NOW(), NOW()),
       (8, 4, 1, 'MEMBER', NOW(), NOW()),
       (9, 4, 2, 'MEMBER', NOW(), NOW()),
       (10, 4, 3, 'MEMBER', NOW(), NOW()),
       (11, 4, 4, 'ADMIN', NOW(), NOW())
;
/*!40000 ALTER TABLE `chat_room_member` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` (`chat_message_id`, `chat_room_id`, `user_id`, `content`, `created_at`, `updated_at`)
VALUES (1, 1, 1, '안녕하세요~', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
       (2, 1, 2, '안녕하세요~', '2024-01-01 10:01:00', '2024-01-01 10:01:00'),
       (3, 2, 1, '안녕하세요~', '2024-01-01 10:02:00', '2024-01-01 10:02:00'),
       (4, 2, 2, '네 반갑습니다.', '2024-01-01 10:03:00', '2024-01-01 10:03:00'),
       (5, 3, 1, '안녕하세요~', '2024-01-01 10:04:00', '2024-01-01 10:04:00'),
       (6, 3, 2, '잘 부탁드립니다!', '2024-01-01 10:05:00', '2024-01-01 10:05:00'),
       (7, 3, 3, '반갑습니다~', '2024-01-01 10:06:00', '2024-01-01 10:06:00'),
       (8, 4, 1, '안녕하세요~', '2024-01-01 10:07:00', '2024-01-01 10:07:00'),
       (9, 4, 2, '저는 백엔드입니다', '2024-01-01 10:08:00', '2024-01-01 10:08:00'),
       (10, 4, 3, '저는 프론트입니다', '2024-01-01 10:09:00', '2024-01-01 10:09:00'),
       (11, 4, 4, '저는 모바일입니다', '2024-01-01 10:10:00', '2024-01-01 10:10:00'),
       (12, 4, 4, '저는 모바일입니다22', '2024-01-01 10:11:00', '2024-01-01 10:11:00')
;
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK
TABLES;

-- 검사 재활성화
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

SET
FOREIGN_KEY_CHECKS = 1;
