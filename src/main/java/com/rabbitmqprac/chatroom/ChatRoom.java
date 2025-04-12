package com.rabbitmqprac.chatroom;

import jakarta.persistence.*;
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
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_Room_id")
    private Long id;

    public static ChatRoom create() {
        return new ChatRoom();
    }

}
