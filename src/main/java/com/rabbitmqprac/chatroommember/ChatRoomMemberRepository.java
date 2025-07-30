package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByUserId(Long memberId);
    Optional<ChatRoomMember> findByChatRoomIdAndUserId(Long chatRoomId, Long memberId);
    int countByChatRoomId(Long chatRoomId);
    List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
}
