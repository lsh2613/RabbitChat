package com.rabbitmqprac.chatmessage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    boolean existsByChatRoomIdAndCreatedAtAfter(Long chatRoomId, LocalDateTime lastEntryTime);

    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    int countByChatRoomIdAndCreatedAtAfter(Long id, LocalDateTime lastEntryTime);
}
