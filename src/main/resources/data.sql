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
VALUES (1, 'user_1', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '귀여운토끼', 'USER', NOW(), NOW()),
       (2, 'user_2', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '용감한호랑이', 'USER', NOW(), NOW()),
       (3, 'user_3', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '지혜로운부엉이', 'USER', NOW(), NOW()),
       (4, 'user_4', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '빠른치타', 'USER', NOW(), NOW()),
       (5, 'user_5', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '성실한개미', 'USER', NOW(), NOW()),
       (6, 'user_6', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '재치있는여우', 'USER', NOW(), NOW()),
       (7, 'user_7', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '강인한곰', 'USER', NOW(), NOW()),
       (8, 'user_8', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '우아한백조', 'USER', NOW(), NOW()),
       (9, 'user_9', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '영리한돌고래', 'USER', NOW(), NOW()),
       (10, 'user_10', '$2a$10$gcUz6PGJLUrSt1TF6/rnc.FLqJPzevnkzDx2yY2Y49ehhuAxhQ4WW', '신속한매', 'USER', NOW(), NOW()),
       (11, 'admin', '$2a$10$KfN5M7d9YNGfK/7oQkHzFeLeG6eFv.zbDHL7qN9fUbbdS3G6wOEqK', 'ADMIN', 'ADMIN', NOW(), NOW()),
       (12, 'guest', '$2a$10$OrK25aG4HqYbXk4NHtMzoOPF9V2B3oFfjkqhv8o1cX1j8xFWXZoe2', 'GUEST', 'USER', NOW(), NOW());
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_room` WRITE;
/*!40000 ALTER TABLE `chat_room` DISABLE KEYS */;
INSERT INTO `chat_room` (`chat_room_id`, `title`, `max_capacity`, `created_at`, `updated_at`)
VALUES (1, '노래 추천좀', 4, NOW(), NOW()),
       (2, '점메추', 3, NOW(), NOW()),
       (3, '재밌는 사람만', 10, NOW(), NOW()),
       (4, '고민 들어줄 사람 있나요?', 2, NOW(), NOW()),
       (5, '면접 준비 도와줄사람?', 3, NOW(), NOW()),
       (6, '같이 노래방 갈사람 ㄱㄱ', 10, NOW(), NOW())
;

/*!40000 ALTER TABLE `chat_room` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_room_member` WRITE;
/*!40000 ALTER TABLE `chat_room_member` DISABLE KEYS */;
INSERT INTO `chat_room_member` (`chat_room_member_id`, `chat_room_id`, `user_id`, `role`, `created_at`, `updated_at`)
VALUES
-- 1번방: 노래 추천좀
       (1, 1, 1, 'ADMIN', '2025-08-31 10:00:00', '2025-08-31 10:00:00'),
       (2, 1, 4, 'MEMBER', '2025-08-31 10:01:00', '2025-08-31 10:01:00'),
       (3, 1, 5, 'MEMBER', '2025-08-31 10:02:00', '2025-08-31 10:02:00'),
-- 2번방: 점메추
       (4, 2, 2, 'ADMIN', '2025-08-31 10:03:00', '2025-08-31 10:03:00'),
       (5, 2, 6, 'MEMBER', '2025-08-31 10:04:00', '2025-08-31 10:04:00'),
       (6, 2, 7, 'MEMBER', '2025-08-31 10:05:00', '2025-08-31 10:05:00'),
-- 3번방: 재밌는 사람만
       (7, 3, 3, 'ADMIN', '2025-08-31 10:06:00', '2025-08-31 10:06:00'),
       (8, 3, 8, 'MEMBER', '2025-08-31 10:07:00', '2025-08-31 10:07:00'),
       (9, 3, 9, 'MEMBER', '2025-08-31 10:08:00', '2025-08-31 10:08:00'),
-- 4번방: 고민 들어줄 사람 있나요?
       (10, 4, 4, 'ADMIN', '2025-08-31 10:09:00', '2025-08-31 10:09:00'),
       (11, 4, 10, 'MEMBER', '2025-08-31 10:10:00', '2025-08-31 10:10:00'),
       (12, 4, 11, 'MEMBER', '2025-08-31 10:11:00', '2025-08-31 10:11:00'),
-- 5번방: 면접 준비 도와줄사람?
       (13, 5, 1, 'ADMIN', '2025-08-31 10:12:00', '2025-08-31 10:12:00'),
       (14, 5, 5, 'MEMBER', '2025-08-31 10:13:00', '2025-08-31 10:13:00'),
       (15, 5, 6, 'MEMBER', '2025-08-31 10:14:00', '2025-08-31 10:14:00'),
-- 6번방: 같이 노래방 갈사람 ㄱㄱ
       (16, 6, 2, 'ADMIN', '2025-08-31 10:15:00', '2025-08-31 10:15:00'),
       (17, 6, 7, 'MEMBER', '2025-08-31 10:16:00', '2025-08-31 10:16:00'),
       (18, 6, 8, 'MEMBER', '2025-08-31 10:17:00', '2025-08-31 10:17:00');
/*!40000 ALTER TABLE `chat_room_member` ENABLE KEYS */;
UNLOCK
TABLES;

LOCK
TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` (`chat_message_id`, `chat_room_id`, `user_id`, `content`, `created_at`, `updated_at`)
VALUES
-- 1번방: 노래 추천좀
    (1, 1, 4, '요즘 듣기 좋은 노래 뭐 있어?', '2025-08-31 10:00:00', '2025-08-31 10:00:00'),
    (2, 1, 5, '저는 뉴진스 노래 추천해요!', '2025-08-31 10:01:00', '2025-08-31 10:01:00'),
    (3, 1, 1, '잔나비 노래도 좋아요!', '2025-08-31 10:02:00', '2025-08-31 10:02:00'),
    (4, 1, 4, '혹시 발라드 추천해줄 사람?', '2025-08-31 10:03:00', '2025-08-31 10:03:00'),
    (5, 1, 5, '폴킴 노래 들어보세요!', '2025-08-31 10:04:00', '2025-08-31 10:04:00'),

-- 2번방: 점메추
    (6, 2, 6, '오늘 점심 뭐 먹지?', '2025-08-31 10:05:00', '2025-08-31 10:05:00'),
    (7, 2, 7, '김치찌개 어때요?', '2025-08-31 10:06:00', '2025-08-31 10:06:00'),
    (8, 2, 2, '저는 샐러드 추천!', '2025-08-31 10:07:00', '2025-08-31 10:07:00'),

-- 3번방: 재밌는 사람만
    (9, 3, 8, '재밌는 얘기 해줄 사람?', '2025-08-31 10:08:00', '2025-08-31 10:08:00'),
    (10, 3, 9, '어제 축구 봤어요? 완전 웃겼음!', '2025-08-31 10:09:00', '2025-08-31 10:09:00'),
    (11, 3, 3, '밈 공유해요~', '2025-08-31 10:10:00', '2025-08-31 10:10:00'),
    (12, 3, 8, '오늘 회사에서 있었던 썰 풀어봄', '2025-08-31 10:11:00', '2025-08-31 10:11:00'),

-- 4번방: 고민 들어줄 사람 있나요?
    (13, 4, 10, '요즘 진로 고민이 많아요...', '2025-08-31 10:12:00', '2025-08-31 10:12:00'),
    (14, 4, 11, '무슨 고민인지 말해봐요!', '2025-08-31 10:13:00', '2025-08-31 10:13:00'),
    (15, 4, 4, '저도 비슷한 고민 있었어요', '2025-08-31 10:14:00', '2025-08-31 10:14:00'),

-- 5번방: 면접 준비 도와줄사람?
    (16, 5, 5, '면접 준비 어떻게 해야 할까요?', '2025-08-31 10:15:00', '2025-08-31 10:15:00'),
    (17, 5, 6, '자기소개 연습해봤어요?', '2025-08-31 10:16:00', '2025-08-31 10:16:00'),
    (18, 5, 1, '기술 면접 팁 공유해요!', '2025-08-31 10:17:00', '2025-08-31 10:17:00'),
    (19, 5, 5, '면접 질문 리스트 있어요?', '2025-08-31 10:18:00', '2025-08-31 10:18:00'),

-- 6번방: 같이 노래방 갈사람 ㄱㄱ
    (20, 6, 7, '노래방 언제 갈까요?', '2025-08-31 10:19:00', '2025-08-31 10:19:00'),
    (21, 6, 8, '주말에 시간 되는 사람?', '2025-08-31 10:20:00', '2025-08-31 10:20:00'),
    (22, 6, 2, '노래방에서 부를 곡 추천해요!', '2025-08-31 10:21:00', '2025-08-31 10:21:00'),
    (23, 6, 7, '2차로 밥도 먹을래요?', '2025-08-31 10:22:00', '2025-08-31 10:22:00'),
    (24, 6, 8, '노래방 예약은 누가 할까요?', '2025-08-31 10:23:00', '2025-08-31 10:23:00'),
    (25, 6, 2, '다들 어떤 노래 좋아해요?', '2025-08-31 10:24:00', '2025-08-31 10:24:00');
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
