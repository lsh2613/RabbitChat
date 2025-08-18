package com.rabbitmqprac.domain.persistence.chatroom.entity;

import com.rabbitmqprac.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@Entity
@Table(name = "ChatRoom")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer maxCapacity;

    public static ChatRoom of(String title, Integer maxCapacity) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.title = title;
        chatRoom.maxCapacity = maxCapacity;
        return chatRoom;
    }

}
