package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByMemberId(Long memberId);
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
    int countByChatRoomId(Long chatRoomId);
    List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);
    boolean existsByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
