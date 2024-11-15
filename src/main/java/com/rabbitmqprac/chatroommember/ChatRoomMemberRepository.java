package com.rabbitmqprac.chatroommember;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByMemberId(Long memberId);
}
