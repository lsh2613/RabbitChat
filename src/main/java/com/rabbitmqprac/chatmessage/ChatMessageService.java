package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.common.dto.ChatMessageRes;
import com.rabbitmqprac.common.dto.ChatSyncRequestRes;
import com.rabbitmqprac.common.dto.MessageRes;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.util.RedisChatUtil;
import com.rabbitmqprac.util.StompHeaderAccessorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.rabbitmqprac.common.dto.ChatDto.ChatMessageReq;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final EntityFacade entityFacade;
    private final ChatMessageRepository chatMessageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RedisChatUtil redisChatUtil;
    private final StompHeaderAccessorUtil stompHeaderAccessorUtil;

    private static final String ROUTING_KEY_PREFIX = "room.";

    public void sendMessage(StompHeaderAccessor accessor, ChatMessageReq req) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatMessage chatMessage = req.createChatMessage(chatRoom.getId(), member.getId());
        chatMessageRepository.save(chatMessage);

        int unreadCnt = calculateUnreadCnt(chatRoom);
        sendMessage(chatMessage, unreadCnt, chatRoom);
    }

    private void sendMessage(ChatMessage chatMessage, int unreadCnt, ChatRoom chatRoom) {
        MessageRes messageRes = ChatMessageRes.createRes(chatMessage, unreadCnt);
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoom.getId(), messageRes);
    }

    private int calculateUnreadCnt(ChatRoom chatRoom) {
        int onlineMemberCnt = redisChatUtil.getOnlineMemberCntInChatRoom(chatRoom.getId());
        int unreadCnt = chatRoom.getChatRoomMemberCnt() - onlineMemberCnt;
        return unreadCnt;
    }

    public List<MessageRes> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        Set<Long> onlineMembersInChatRoom = redisChatUtil.getOnlineMembers(chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        List<MessageRes> messageResList = chatMessages.stream()
                .map(chatMessage -> {
                    int unreadCnt = chatRoom.getUnreadCnt(onlineMembersInChatRoom, chatMessage.getCreatedAt());
                    return ChatMessageRes.createRes(chatMessage, unreadCnt);
                })
                .toList();

        return messageResList;
    }

    public void handleConnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.getMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.getChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        enterChatRoom(chatRoom.getId(), member.getId());
        readUnreadMessages(chatRoom, member.getId());
    }

    private void enterChatRoom(Long chatRoomId, Long memberId) {
        redisChatUtil.addChatRoom2Member(chatRoomId, memberId);
    }

    private void readUnreadMessages(ChatRoom chatRoom, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(memberId);
        LocalDateTime lastEntryTime = chatRoomMember.getLastEntryTime();

        boolean existsUnreadMessage = chatMessageRepository.existsByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), lastEntryTime);
        boolean existsOnlineChatRoomMember = redisChatUtil.getOnlineMemberCntInChatRoom(chatRoom.getId()) > 1; // 1은 본인

        if (existsUnreadMessage && existsOnlineChatRoomMember)
            sendChatSyncRequestMessage(chatRoom.getId());
    }

    private void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = ChatSyncRequestRes.createRes();
        rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX + chatRoomId, messageRes);
    }

    public void handleDisconnectMessage(StompHeaderAccessor accessor) {
        Long memberId = stompHeaderAccessorUtil.removeMemberIdInSession(accessor);
        Member member = entityFacade.getMember(memberId);

        Long chatRoomId = stompHeaderAccessorUtil.removeChatRoomIdInSession(accessor);
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoom.getChatRoomMember(member.getId());
        chatRoomMember.updateLastEntryTime();

        exitChatRoom(chatRoom, member);
    }

    private void exitChatRoom(ChatRoom chatRoom, Member member) {
        redisChatUtil.removeChatRoom2Member(chatRoom.getId(), member.getId());
    }
}
