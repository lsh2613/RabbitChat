package com.rabbitmqprac.domain.persistence.chatmessage.entity;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.common.model.DateAuditable;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class ChatMessage extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    public static ChatMessage of(ChatRoom chatRoom, User user, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.chatRoom = chatRoom;
        chatMessage.user = user;
        chatMessage.content = content;
        return chatMessage;
    }

    public static ChatMessage of(Long id, ChatRoom chatRoom, User user, String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id = id;
        chatMessage.chatRoom = chatRoom;
        chatMessage.user = user;
        chatMessage.content = content;
        return chatMessage;
    }
}
