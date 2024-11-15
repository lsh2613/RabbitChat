package com.rabbitmqprac.chatroom;

import com.rabbitmqprac.chatroommember.ChatRoomMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "ChatRoom")
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_Room_id")
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> chatRoomMembers = new HashSet<>();

    public static ChatRoom emptyChatRoom() {
        return new ChatRoom();
    }

    public void addChatRoomMember(ChatRoomMember chatRoomMember) {
        if (Optional.ofNullable(chatRoomMembers).isEmpty()) {
            chatRoomMembers = new HashSet<>();
        }
        if (chatRoomMember.getChatRoom() != this)
            chatRoomMember.addChatRoom(this);
        this.chatRoomMembers.add(chatRoomMember);
    }

    public int getChatRoomMemberCnt() {
        if (Optional.ofNullable(chatRoomMembers).isEmpty())
            return 0;
        return chatRoomMembers.size();
    }

    public ChatRoomMember getChatRoomMember(Long memberId) {
        return chatRoomMembers.stream()
                .filter(chatRoomMember -> chatRoomMember.getMember().getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("채팅방 참가자를 찾을 수 없습니다"));
    }

    public int getUnreadCnt(Set<Long> onlineMembers, LocalDateTime messageCreatedAt) {
        List<LocalDateTime> lastEntryTimes = getLastEntryTimesExcludingOnlineMembers(onlineMembers);

        int unreadMemberCount = (int) lastEntryTimes.stream()
                .filter(time -> time.isAfter(messageCreatedAt))
                .count();

        return getChatRoomMemberCnt() - onlineMembers.size() - unreadMemberCount;
    }

    private List<LocalDateTime> getLastEntryTimesExcludingOnlineMembers(Set<Long> onlineMemberIds) {
        return chatRoomMembers.stream()
                .filter(chatRoomMember -> !onlineMemberIds.contains(chatRoomMember.getMember().getId()))
                .map(ChatRoomMember::getLastEntryTime)
                .toList();
    }

}
