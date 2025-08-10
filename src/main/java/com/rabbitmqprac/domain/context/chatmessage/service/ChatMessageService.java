package com.rabbitmqprac.domain.context.chatmessage.service;

import com.rabbitmqprac.application.dto.chatmessage.req.ChatMessageReq;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageDetailRes;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatMessageRes;
import com.rabbitmqprac.application.mapper.ChatMessageMapper;
import com.rabbitmqprac.domain.context.chatmessagestatus.service.ChatMessageStatusService;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatmessage.repository.ChatMessageRepository;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserSession;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserStatus;
import com.rabbitmqprac.global.helper.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final EntityFacade entityFacade;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageStatusService chatMessageStatusService;
    private final UserSessionService userSessionService;
    private final ChatMessageRepository chatMessageRepository;
    private final RabbitPublisher rabbitPublisher;

    @Transactional
    public void sendMessage(Long userId, Long chatRoomId, ChatMessageReq req) {
        User user = entityFacade.readUser(userId);
        ChatRoom chatRoom = entityFacade.readChatRoom(chatRoomId);

        ChatMessage chatMessage = saveChatMessage(req, chatRoom, user);

        int unreadMemberCnt = calculateUnreadMemberCntAtSending(chatRoom.getId());
        sendMessage(chatMessage, unreadMemberCnt, chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDetailRes> readChatMessagesBefore(Long chatRoomId, Long lastChatMessageId, int size) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(
                chatRoomId, lastChatMessageId, size + 1
        );

        boolean hasNext = chatMessages.size() > size;
        if (hasNext) {
            chatMessages = chatMessages.subList(0, size);
        }

        return covertToDetailRes(chatRoomId, chatMessages);
    }

    private ChatMessage saveChatMessage(ChatMessageReq req, ChatRoom chatRoom, User user) {
        ChatMessage chatMessage = req.toChatMessage(chatRoom, user);
        return chatMessageRepository.save(chatMessage);
    }

    /**
     * 메시지 전송 시점에 해당 메시지에 대해 읽지 않은 유저의 수를 계산한다.
     *
     * @return 채팅방 전체 인원 - 현재 채팅방에 접속 중인 인원
     */
    private int calculateUnreadMemberCntAtSending(Long chatRoomId) {
        Set<Long> memberIds = getChatRoomMemberIds(chatRoomId);
        List<UserSession> userSessions = getUserSessions(memberIds);
        Set<Long> activeMemberIds = getActiveMemberIds(userSessions, chatRoomId);

        // todo UserSessions 중 status = ACTIVE_CHAT_ROOM_LIST or ACTIVE_CHAT_ROOM이 아닌 유저에게 푸시 알림

        return memberIds.size() - activeMemberIds.size();
    }

    /**
     * 메시지 조회 시점에 각 메시지에 대해 읽지 않은 유저의 수 계산한다.
     *
     * @return 채팅방 전체 인원 - 현재 채팅방에 접속 중인 인원 - 비접속 인원 중 마지막으로 읽은 메시지가 chatMessageId보다 큰 인원
     */
    private int calculateUnreadMemberCntAtReading(Long chatRoomId, Long chatMessageId) {
        Set<Long> memberIds = getChatRoomMemberIds(chatRoomId);
        List<UserSession> userSessions = getUserSessions(memberIds);
        Set<Long> activeMemberIds = getActiveMemberIds(userSessions, chatRoomId);
        Set<Long> inactiveMemberIds = getInactiveMemberIds(memberIds, activeMemberIds);

        List<Long> lastReadMessageIds = inactiveMemberIds.stream()
                .map(userId -> chatMessageStatusService.readLastReadMessageId(userId, chatRoomId))
                .toList();


        int memberCntGraterThanChatMessageId = (int) lastReadMessageIds.stream()
                .filter(lastReadMessageId -> lastReadMessageId > chatMessageId)
                .count();

        return memberIds.size() - activeMemberIds.size() - memberCntGraterThanChatMessageId;
    }

    private List<UserSession> getUserSessions(Set<Long> memberIds) {
        List<UserSession> userSessions = memberIds.stream()
                .map(userSessionService::read)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return userSessions;
    }

    private Set<Long> getChatRoomMemberIds(Long chatRoomId) {
        return chatRoomMemberService.readUserIdsByChatRoomId(chatRoomId);
    }

    private Set<Long> getActiveMemberIds(List<UserSession> userSessions, Long chatRoomId) {
        return userSessions.stream()
                .filter(userSession ->
                        userSession.getStatus().equals(UserStatus.ACTIVE_CHAT_ROOM)
                                && userSession.getCurrentChatRoomId().equals(chatRoomId)
                )
                .map(UserSession::getUserId)
                .collect(Collectors.toSet());
    }

    private static Set<Long> getInactiveMemberIds(Set<Long> memberIds, Set<Long> onlineMemberIds) {
        Set<Long> offlineMemberIds = memberIds.stream()
                .filter(id -> !onlineMemberIds.contains(id))
                .collect(Collectors.toSet());
        return offlineMemberIds;
    }

    private void sendMessage(ChatMessage chatMessage, int unreadMemberCnt, ChatRoom chatRoom) {
        ChatMessageRes chatMessageRes = ChatMessageMapper.toRes(chatMessage, unreadMemberCnt);
        rabbitPublisher.publish(chatRoom.getId(), chatMessageRes);
    }

    @Transactional(readOnly = true)
    public Optional<ChatMessage> readLastChatMessage(Long chatRoomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId);
    }

    @Transactional(readOnly = true)
    public int countUnreadMessages(Long chatRoomId, Long lastReadMessageId) {
        return chatMessageRepository.countByChatRoomIdAndIdGreaterThan(chatRoomId, lastReadMessageId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDetailRes> readChatMessagesBetween(Long userId, Long chatRoomId, Long from, Long to) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdAndIdBetween(chatRoomId, from, to);

        List<ChatMessageDetailRes> result = covertToDetailRes(chatRoomId, chatMessages);
        if (!result.isEmpty()) {
            Long lastElementId = result.getLast().chatMessageId();
            if (existsUnreadMessage(userId, chatRoomId, lastElementId)) {
                chatMessageStatusService.saveLastReadMessageId(userId, chatRoomId, lastElementId);
                // todo 만약 해당 채팅방에 activeMember가 존재한다면 데이터 싱크를 맞추기 위해 ChatSyncRequestMessage 전송
            }
        }

        return result;
    }

    private List<ChatMessageDetailRes> covertToDetailRes(Long chatRoomId, List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(chatMessage -> {
                    int unreadMemberCnt = calculateUnreadMemberCntAtReading(chatRoomId, chatMessage.getId());
                    return ChatMessageMapper.toDetailRes(chatMessage, unreadMemberCnt);
                })
                .toList();
    }

    private boolean existsUnreadMessage(Long userId, Long chatRoomId, Long lastElementId) {
        Long lastReadMessageId = chatMessageStatusService.readLastReadMessageId(userId, chatRoomId);

        return lastElementId > lastReadMessageId;
    }
}
