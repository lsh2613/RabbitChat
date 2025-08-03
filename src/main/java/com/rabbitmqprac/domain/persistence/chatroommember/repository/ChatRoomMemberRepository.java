package com.rabbitmqprac.domain.persistence.chatroommember.repository;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByUserId(Long userId);
    Optional<ChatRoomMember> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
    int countByChatRoomId(Long chatRoomId);
    List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
}
