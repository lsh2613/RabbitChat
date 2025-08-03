package com.rabbitmqprac.domain.persistence.chatroommember.entity;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatRoomMember")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ChatRoomMemberRole role;

    private LocalDateTime lastExitAt;

    public static ChatRoomMember of(ChatRoom chatRoom, User user, ChatRoomMemberRole role) {
        ChatRoomMember chatRoomMember = new ChatRoomMember();
        chatRoomMember.setChatRoom(chatRoom);
        chatRoomMember.setUser(user);
        chatRoomMember.setRole(role);
        chatRoomMember.setLastExitAt(LocalDateTime.now());
        return chatRoomMember;
    }

    public void updateLastExitAt() {
        this.lastExitAt = LocalDateTime.now();
    }

}
