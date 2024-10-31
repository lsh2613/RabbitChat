package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.user.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ChatMessage")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // @EqualsAndHashCode.Include이 담긴 필드로 equals, hash를 만듦
@EntityListeners(value = {AuditingEntityListener.class}) //@CreateDate 사용해주기 위함
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId", insertable = false, updatable = false)
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;

    private String message;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

}
