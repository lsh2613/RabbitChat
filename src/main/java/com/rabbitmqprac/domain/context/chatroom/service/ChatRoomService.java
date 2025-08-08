package com.rabbitmqprac.domain.context.chatroom.service;


import com.rabbitmqprac.application.dto.chatroom.req.ChatRoomCreateReq;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomDetailRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomInfoRes;
import com.rabbitmqprac.application.mapper.ChatMessageMapper;
import com.rabbitmqprac.application.mapper.ChatRoomMapper;
import com.rabbitmqprac.domain.context.chatmessage.service.ChatMessageService;
import com.rabbitmqprac.domain.context.chatmessagestatus.service.ChatMessageStatusService;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final EntityFacade entityFacade;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatMessageStatusService chatMessageStatusService;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomDetailRes create(Long userId, ChatRoomCreateReq req) {
        User user = entityFacade.readUser(userId);

        ChatRoom chatRoom = saveChatRoom(req.title(), req.maxCapacity());
        chatRoomMemberService.createAdmin(user, chatRoom);

        return ChatRoomMapper.toDetailRes(chatRoom);
    }

    private ChatRoom saveChatRoom(String title, Integer maxCapacity) {
        return chatRoomRepository.save(ChatRoom.of(title, maxCapacity));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDetailRes> getMyChatRooms(Long userId) {
        User user = entityFacade.readUser(userId);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberService.readChatRoomMembersByUserId(user.getId());

        List<ChatRoomDetailRes> chatRoomDetailResList = chatRoomMembers.stream().map(chatroomMember -> {
                    ChatRoom chatRoom = chatroomMember.getChatRoom();

                    Optional<ChatMessage> lastMessage = chatMessageService.readLastChatMessage(chatRoom.getId());
                    int chatRoomMemberCount = chatRoomMemberService.countChatRoomMembers(chatRoom.getId());
                    Long lastReadMessageId = chatMessageStatusService.readLastReadMessageId(userId, chatRoom.getId());
                    int unreadMessageCount = chatMessageService.countUnreadMessages(chatRoom.getId(), lastReadMessageId);

                    return ChatRoomMapper.toDetailRes(
                            chatRoom,
                            lastMessage.map(ChatMessageMapper::toLastDetailRes).orElse(null),
                            chatRoomMemberCount,
                            lastReadMessageId,
                            unreadMessageCount
                    );
                })
                .toList();

        return chatRoomDetailResList;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomInfoRes> getChatRooms(Optional<Long> userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        return chatRooms.stream()
                .map(chatRoom -> {
                    int chatRoomMemberCount = chatRoomMemberService.countChatRoomMembers(chatRoom.getId());
                    boolean isJoined = userId.isPresent() && chatRoomMemberService.isExists(chatRoom.getId(), userId.get());
                    return ChatRoomMapper.toInfoRes(chatRoom, chatRoomMemberCount, isJoined);
                })
                .toList();
    }
}
