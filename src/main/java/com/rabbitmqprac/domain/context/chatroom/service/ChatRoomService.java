package com.rabbitmqprac.domain.context.chatroom.service;


import com.rabbitmqprac.domain.persistence.chatmessage.entity.ChatMessage;
import com.rabbitmqprac.domain.persistence.chatmessage.repository.ChatMessageRepository;
import com.rabbitmqprac.application.dto.chatmessage.res.ChatSyncRequestRes;
import com.rabbitmqprac.application.dto.chatmessage.res.MessageRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomCreateRes;
import com.rabbitmqprac.application.dto.chatroom.res.ChatRoomRes;
import com.rabbitmqprac.application.dto.chatroom.res.MyChatRoomRes;
import com.rabbitmqprac.domain.persistence.chatroom.entity.ChatRoom;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRepository;
import com.rabbitmqprac.domain.persistence.chatroommember.entity.ChatRoomMember;
import com.rabbitmqprac.domain.persistence.chatroommember.repository.ChatRoomMemberRepository;
import com.rabbitmqprac.domain.context.chatroommember.service.ChatRoomMemberService;
import com.rabbitmqprac.domain.context.common.service.EntityFacade;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.RabbitPublisher;
import com.rabbitmqprac.domain.persistence.chatroom.repository.ChatRoomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final EntityFacade entityFacade;
    private final RabbitPublisher rabbitPublisher;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomCreateRes createChatRoom(Long memberId) {
        User user = entityFacade.getUser(memberId);

        ChatRoom chatRoom = saveChatRoom();
        chatRoomMemberService.addChatRoomMember(chatRoom.getId(), user.getId());

        return ChatRoomCreateRes.createRes(chatRoom.getId(), user.getId());
    }

    private ChatRoom saveChatRoom() {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create());
        return chatRoom;
    }

    @Transactional(readOnly = true)
    public List<MyChatRoomRes> getMyChatRooms(Long memberId) {
        User user = entityFacade.getUser(memberId);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByUserId(user.getId());

        List<MyChatRoomRes> myChatRoomResList = chatRoomMembers.stream().map(chatroomMember -> {
                    ChatRoom chatRoom = chatroomMember.getChatRoom();

                    Optional<ChatMessage> lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
                    int unreadMessageCnt = chatMessageRepository.countByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), chatroomMember.getLastExitAt());
                    int chatRoomMemberCnt = chatRoomMemberRepository.countByChatRoomId(chatRoom.getId());

                    return MyChatRoomRes.createRes(chatRoom.getId(), chatRoomMemberCnt, unreadMessageCnt, lastMessage);
                })
                .toList();

        return myChatRoomResList;
    }

    @Transactional(readOnly = true)
    public List<ChatRoomRes> getChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        return chatRooms.stream().map(ChatRoomRes::of).toList();

    }

    public void enterChatRoom(Long memberId, Long chatRoomId) {
        User user = entityFacade.getUser(memberId);

        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        chatRoomRedisRepository.enterChatRoom(chatRoomId, memberId);
        readUnreadMessages(chatRoom, user.getId());
    }

    private void readUnreadMessages(ChatRoom chatRoom, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndUserId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new RuntimeException("채팅방 참가자를 찾을 수 없습니다"));
        LocalDateTime lastExitAt = chatRoomMember.getLastExitAt();

        boolean existsUnreadMessage = chatMessageRepository.existsByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), lastExitAt);
        boolean existsOnlineChatRoomMember = chatRoomRedisRepository.getOnlineChatRoomMemberCnt(chatRoom.getId()) > 1; // 1은 본인

        if (existsUnreadMessage && existsOnlineChatRoomMember)
            sendChatSyncRequestMessage(chatRoom.getId());
    }

    public void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = ChatSyncRequestRes.createRes();
        rabbitPublisher.publish(chatRoomId, messageRes);
    }

    @Transactional
    public void exitChatRoom(Long memberId, Long chatRoomId) {
        User user = entityFacade.getUser(memberId);

        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndUserId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new RuntimeException("채팅방 참가자를 찾을 수 없습니다"));

        chatRoomMember.updateLastExitAt();

        chatRoomRedisRepository.exitChatRoom(chatRoom.getId(), user.getId());
    }
}
