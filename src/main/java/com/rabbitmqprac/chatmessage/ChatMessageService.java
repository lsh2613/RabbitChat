package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.common.dto.ChatMessageRes;
import com.rabbitmqprac.common.dto.MessageRes;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.util.RabbitPublisher;
import com.rabbitmqprac.util.RedisChatUtil;
import com.rabbitmqprac.util.StompHeaderAccessorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.rabbitmqprac.common.dto.ChatDto.ChatMessageReq;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final EntityFacade entityFacade;
    private final ChatMessageRepository chatMessageRepository;
    private final RabbitPublisher rabbitPublisher;
    private final RedisChatUtil redisChatUtil;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    @Transactional
    public void sendMessage(StompHeaderAccessor accessor, ChatMessageReq req) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatMessage chatMessage = saveChatMessage(req, chatRoom, member);

        int unreadCnt = calculateUnreadCnt(chatRoom);
        sendMessage(chatMessage, unreadCnt, chatRoom);
    }

    @Transactional(readOnly = true)
    public List<MessageRes> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        Set<Long> onlineChatRoomMembers = redisChatUtil.getOnlineChatRoomMembers(chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        List<MessageRes> messageResList = chatMessages.stream()
                .map(chatMessage -> {
                    int unreadCnt = chatRoom.getUnreadCnt(onlineChatRoomMembers, chatMessage.getCreatedAt());
                    return ChatMessageRes.createRes(chatMessage, unreadCnt);
                })
                .toList();

        return messageResList;
    }

    private ChatMessage saveChatMessage(ChatMessageReq req, ChatRoom chatRoom, Member member) {
        ChatMessage chatMessage = req.createChatMessage(chatRoom.getId(), member.getId());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    private int calculateUnreadCnt(ChatRoom chatRoom) {
        int onlineChatRoomMemberCnt = redisChatUtil.getOnlineChatRoomMemberCnt(chatRoom.getId());
        int unreadCnt = chatRoom.getChatRoomMemberCnt() - onlineChatRoomMemberCnt;
        return unreadCnt;
    }

    private void sendMessage(ChatMessage chatMessage, int unreadCnt, ChatRoom chatRoom) {
        MessageRes messageRes = ChatMessageRes.createRes(chatMessage, unreadCnt);
        rabbitPublisher.publish(chatRoom.getId(), messageRes);
    }
}
