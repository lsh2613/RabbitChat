package com.rabbitmqprac.infra.batch;

import com.rabbitmqprac.common.container.BatchTestContainer;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.entity.ChatMessageStatus;
import com.rabbitmqprac.domain.persistence.chatmessagestatus.repository.ChatMessageStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class LastMessageIdBatchTest extends BatchTestContainer {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private Job lastMessageIdJob;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ChatMessageStatusRepository chatMessageStatusRepository;

    private JobParameters jobParameters;

    private static final Long userId = 1L;
    private static final Long userId2 = 2L;
    private static final Long roomId = 1L;
    private static final Long roomId2 = 2L;

    @BeforeEach
    void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
        jobLauncherTestUtils.setJob(lastMessageIdJob);
        jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // Redis 테스트 데이터 설정
        redisTemplate.opsForValue().set(generateKey(userId, roomId), "100");
        redisTemplate.opsForValue().set(generateKey(userId, roomId2), "200");
        redisTemplate.opsForValue().set(generateKey(userId2, roomId), "300");
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        // Redis 데이터 정리
        Set<String> keys = redisTemplate.keys("CHAT_ROOM::*::USER::*::LAST_READ");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        // DB 데이터 정리
        chatMessageStatusRepository.deleteAll();
    }

    @Test
    @DisplayName("lastMessageId Job이 정상적으로 실행되어야 한다")
    void lastMessageIdJobTest() throws Exception {
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        List<ChatMessageStatus> statuses = chatMessageStatusRepository.findAllByUserIdAndChatRoomIdIn(
                userId,
                Arrays.asList(roomId, roomId2)
        );

        assertEquals(2, statuses.size());

        // userId1, roomId1 검증
        Optional<ChatMessageStatus> status1 = statuses.stream()
                .filter(s -> s.getChatRoomId().equals(roomId))
                .findFirst();
        assertTrue(status1.isPresent(), "userId1, roomId1 데이터가 존재해야 합니다.");
        assertEquals(100L, status1.get().getLastReadMessageId());

        // userId1, roomId2 검증
        Optional<ChatMessageStatus> status2 = statuses.stream()
                .filter(s -> s.getChatRoomId().equals(roomId2))
                .findFirst();
        assertTrue(status2.isPresent(), "userId1, roomId2 데이터가 존재해야 합니다.");
        assertEquals(200L, status2.get().getLastReadMessageId());

        // Redis 캐시가 남아있는지 확인
        assertEquals("100", redisTemplate.opsForValue().get(generateKey(userId, roomId)));
        assertEquals("200", redisTemplate.opsForValue().get(generateKey(userId, roomId2)));
        assertEquals("300", redisTemplate.opsForValue().get(generateKey(userId2, roomId)));
    }

    @Test
    @DisplayName("Job 실행 시 일부 데이터가 누락되어도 나머지 데이터는 정상 처리되어야 한다")
    void jobWithPartialDataTest() throws Exception {
        // given
        redisTemplate.opsForValue().set(generateKey(userId, roomId), "invalid_value"); // Redis에는 있지만 value가 잘못된 데이터
        redisTemplate.opsForValue().set(generateKey(userId2, roomId2), "300");

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus(), "Job이 정상 완료되어야 합니다.");

        Optional<ChatMessageStatus> validStatus = chatMessageStatusRepository.findByUserIdAndChatRoomId(userId2, roomId2);
        assertTrue(validStatus.isPresent(), "2번 사용자, 2번 채팅방의 lastMessageId 데이터가 존재해야 합니다.");
        assertEquals(300L, validStatus.get().getLastReadMessageId());

        Optional<ChatMessageStatus> invalidStatus = chatMessageStatusRepository.findByUserIdAndChatRoomId(userId, roomId);
        assertTrue(invalidStatus.isEmpty(), "1번 사용자, 1번 채팅방 (잘못된)lastMessageId 데이터가 존재하지 않아야 합니다.");
    }

    @Test
    @DisplayName("빈 데이터로 Job 실행 시 정상 완료되어야 한다")
    void emptyDataJobTest() throws Exception {
        // given
        cleanUp();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus(), "Job이 정상 완료되어야 합니다.");
        assertEquals(0, chatMessageStatusRepository.count());
    }

    @Test
    @DisplayName("대량의 데이터도 정상적으로 처리되어야 한다")
    void largeDataSetTest() throws Exception {
        // given
        int userCount = 100;
        int roomCount = 50;

        // 대량의 테스트 데이터 생성
        for (int i = 1; i <= userCount; i++) {
            for (int j = 1; j <= roomCount; j++) {
                redisTemplate.opsForValue().set(generateKey((long) i, (long) j), String.valueOf(i * 1000 + j));
            }
        }

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(userCount * roomCount, chatMessageStatusRepository.count());

        // 샘플 데이터 검증
        Optional<ChatMessageStatus> sampleStatus = chatMessageStatusRepository
                .findByUserIdAndChatRoomId(50L, 25L);
        assertTrue(sampleStatus.isPresent());
        assertEquals(50000 + 25, sampleStatus.get().getLastReadMessageId());
    }

    @Test
    @DisplayName("Job이 실패하더라도 이전 처리 데이터는 유지되어야 한다")
    void jobFailureTest() throws Exception {
        // given
        redisTemplate.opsForValue().set(generateKey(userId, roomId), "100");
        redisTemplate.opsForValue().set(generateKey(userId, roomId2), "invalid_value"); // 실패 유발 데이터
        redisTemplate.opsForValue().set(generateKey(userId2, roomId), "300");

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        // 정상 데이터는 저장되어 있어야 함
        Optional<ChatMessageStatus> status1 = chatMessageStatusRepository
                .findByUserIdAndChatRoomId(userId, roomId);
        assertTrue(status1.isPresent());
        assertEquals(100L, status1.get().getLastReadMessageId());

        Optional<ChatMessageStatus> status2 = chatMessageStatusRepository
                .findByUserIdAndChatRoomId(userId2, roomId);
        assertTrue(status2.isPresent());
        assertEquals(300L, status2.get().getLastReadMessageId());
    }

    private static final String CACHE_KEY_FORMAT = "CHAT_ROOM::%d::USER::%d::LAST_READ";

    private String generateKey(Long userId, Long chatRoomId) {
        return CACHE_KEY_FORMAT.formatted(chatRoomId, userId);
    }
}
