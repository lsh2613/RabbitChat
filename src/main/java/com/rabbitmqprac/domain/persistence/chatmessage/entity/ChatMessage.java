package com.rabbitmqprac.domain.persistence.chatmessage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "chat_message")
@ToString
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;

    private Long userId;

    private String content;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    public static ChatMessage create(Long chatRoomId, Long userId, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setUserId(userId);
        chatMessage.setContent(message);
        chatMessage.setCreatedAt(LocalDateTime.now());
        return chatMessage;
    }
}
