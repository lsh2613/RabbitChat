package com.rabbitmqprac.chatroom;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c LEFT JOIN FETCH c.chatRoomMembers WHERE c.id = :chatRoomId")
    Optional<ChatRoom> findByIdWithChatRoomMembers(@Param("id") Long chatRoomId);
}
