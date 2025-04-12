package com.rabbitmqprac.chatmessage;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.chatroommember.ChatRoomMemberRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.rabbitmqprac.common.dto.ChatDto.ChatMessageReq;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final EntityFacade entityFacade;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
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

        int unreadCnt = calculateUnreadCntAtPublish(chatRoom.getId());
        sendMessage(chatMessage, unreadCnt, chatRoom);
    }

    @Transactional(readOnly = true)
    public List<MessageRes> getChatMessages(Long chatRoomId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(chatRoom.getId());

        List<MessageRes> messageResList = chatMessages.stream()
                .map(chatMessage -> {
                    /*
                    현재 접속 중인 채팅방 유저를 제외하고 나머지 채팅방 유저의 마지막 입장 시간을 가져옴
                    그 중에서 메시지 생성 시간보다 늦은 시간에 입장한 유저(해당 메시지를 읽지 않았다는 의미)의 수를 세어줌
                    unreadCnt = 채팅방 유저 수 - 현재 접속 중인 유저 수 - 메시지 생성 시간보다 늦은 시간에 입장한 유저 수
                     */
                    int unreadCnt = calculateUnreadCntAtReadTime(chatRoom.getId(), chatMessage.getCreatedAt());
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

    private int calculateUnreadCntAtPublish(Long chatRoomId) {
        int onlineChatRoomMemberCnt = redisChatUtil.getOnlineChatRoomMemberCnt(chatRoomId);
        int chatRoomMemberCnt = chatRoomMemberRepository.countByChatRoomId(chatRoomId);
        int unreadCnt = chatRoomMemberCnt - onlineChatRoomMemberCnt;
        return unreadCnt;
    }

    private int calculateUnreadCntAtReadTime(Long chatRoomId, LocalDateTime messageCreatedAt) {
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByChatRoomId(chatRoomId);
        Set<Long> onlineChatRoomMembers = redisChatUtil.getOnlineChatRoomMembers(chatRoomId);
        List<LocalDateTime> lastEntryTimes = getLastEntryTimesExcludingOnlineMembers(chatRoomMembers, onlineChatRoomMembers);

        int memberCntAfterMessageCreated = (int) lastEntryTimes.stream()
                .filter(time -> time.isAfter(messageCreatedAt))
                .count();

        return chatRoomMembers.size() - onlineChatRoomMembers.size() - memberCntAfterMessageCreated;
    }

    private List<LocalDateTime> getLastEntryTimesExcludingOnlineMembers(List<ChatRoomMember> chatRoomMembers, Set<Long> onlineMemberIds) {
        return chatRoomMembers.stream()
                .filter(chatRoomMember -> !onlineMemberIds.contains(chatRoomMember.getMember().getId()))
                .map(ChatRoomMember::getLastEntryTime)
                .toList();
    }

    private void sendMessage(ChatMessage chatMessage, int unreadCnt, ChatRoom chatRoom) {
        MessageRes messageRes = ChatMessageRes.createRes(chatMessage, unreadCnt);
        rabbitPublisher.publish(chatRoom.getId(), messageRes);
    }
}
