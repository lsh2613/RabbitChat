package com.rabbitmqprac.domain.persistence.chatroom.repository;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
