package com.rabbitmqprac.domain.persistence.chatroommember.repository;

import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findAllByUserId(Long userId);

    int countByChatRoomId(Long chatRoomId);

    List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);

    @Query("""
            select c from ChatRoomMember c join fetch c.user where c.chatRoom.id = :chatRoomId
            """)
    List<ChatRoomMember> findAllWithUserByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);

    boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    @Query("SELECT crm.user.id FROM ChatRoomMember crm WHERE crm.chatRoom.id = :chatRoomId")
    Set<Long> findUserIdsByChatRoomId(Long chatRoomId);
}
