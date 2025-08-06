package com.rabbitmqprac.domain.persistence.chatmessage.repository;

import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    int countByChatRoomIdAndIdGreaterThan(Long chatRoomId, Long lastReadMessageId);

    @Query("""
            SELECT cm 
            FROM ChatMessage cm 
            WHERE cm.chatRoom.id = :chatRoomId AND cm.id > :lastMessageId
            ORDER BY cm.createdAt ASC LIMIT :size
            """)
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId, Long lastMessageId, int size);
}
