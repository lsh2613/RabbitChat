package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.common.ChatDto;
import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroom.ChatRoomRepository;
import com.rabbitmqprac.common.ChatMessageRes;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(ChatDto.ChatMessageReq chatMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageReq.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        Member member = memberRepository.findById(chatMessageReq.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        ChatMessage chatMessage = chatMessageReq.toEntity(chatRoom, member);
        chatRoom.setLastChatMsg(chatMessage);
        chatRoomRepository.save(chatRoom);

        ChatMessageRes chatMessageRes = ChatMessageRes.createRes(
                chatMessage.getChatRoom().getId(),
                chatMessage.getMember().getId(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt());

        rabbitTemplate.convertAndSend("room." + chatMessageReq.getChatRoomId(), chatMessageRes);
        log.info("Message sent to RabbitMQ: {}", chatMessage);
    }
}
