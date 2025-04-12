package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.user.Member;
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
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    private LocalDateTime lastEntryTime;

    public static ChatRoomMember create(ChatRoom chatRoom, Member member) {
        ChatRoomMember chatRoomMember = new ChatRoomMember();
        chatRoomMember.setChatRoom(chatRoom);
        chatRoomMember.setMember(member);
        chatRoomMember.setLastEntryTime(LocalDateTime.now());
        return chatRoomMember;
    }

    public void updateLastEntryTime() {
        this.lastEntryTime = LocalDateTime.now();
    }

}
