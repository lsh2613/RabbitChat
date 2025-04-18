package com.rabbitmqprac.chatroom;


import com.rabbitmqprac.chatmessage.ChatMessage;
import com.rabbitmqprac.chatmessage.ChatMessageRepository;
import com.rabbitmqprac.chatmessage.dto.ChatSyncRequestRes;
import com.rabbitmqprac.chatmessage.dto.MessageRes;
import com.rabbitmqprac.chatroom.dto.ChatRoomCreateRes;
import com.rabbitmqprac.chatroom.dto.ChatRoomRes;
import com.rabbitmqprac.chatroom.dto.MyChatRoomRes;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.chatroommember.ChatRoomMemberRepository;
import com.rabbitmqprac.chatroommember.ChatRoomMemberService;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.member.Member;
import com.rabbitmqprac.util.RabbitPublisher;
import com.rabbitmqprac.util.RedisChatUtil;
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
    private final RedisChatUtil redisChatUtil;
    private final EntityFacade entityFacade;
    private final RabbitPublisher rabbitPublisher;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomCreateRes createChatRoom(Long memberId) {
        Member member = entityFacade.getMember(memberId);

        ChatRoom chatRoom = saveChatRoom();
        chatRoomMemberService.addChatRoomMember(chatRoom.getId(), member.getId());

        return ChatRoomCreateRes.createRes(chatRoom.getId(), member.getId());
    }

    private ChatRoom saveChatRoom() {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.create());
        return chatRoom;
    }

    @Transactional(readOnly = true)
    public List<MyChatRoomRes> getMyChatRooms(Long memberId) {
        Member member = entityFacade.getMember(memberId);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByMemberId(member.getId());

        List<MyChatRoomRes> myChatRoomResList = chatRoomMembers.stream().map(chatroomMember -> {
                    ChatRoom chatRoom = chatroomMember.getChatRoom();

                    Optional<ChatMessage> lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
                    int unreadMessageCnt = chatMessageRepository.countByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), chatroomMember.getLastEntryTime());
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
        Member member = entityFacade.getMember(memberId);

        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        redisChatUtil.enterChatRoom(chatRoomId, memberId);
        readUnreadMessages(chatRoom, member.getId());
    }

    private void readUnreadMessages(ChatRoom chatRoom, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new RuntimeException("채팅방 참가자를 찾을 수 없습니다"));
        LocalDateTime lastEntryTime = chatRoomMember.getLastEntryTime();

        boolean existsUnreadMessage = chatMessageRepository.existsByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), lastEntryTime);
        boolean existsOnlineChatRoomMember = redisChatUtil.getOnlineChatRoomMemberCnt(chatRoom.getId()) > 1; // 1은 본인

        if (existsUnreadMessage && existsOnlineChatRoomMember)
            sendChatSyncRequestMessage(chatRoom.getId());
    }

    public void sendChatSyncRequestMessage(Long chatRoomId) {
        MessageRes messageRes = ChatSyncRequestRes.createRes();
        rabbitPublisher.publish(chatRoomId, messageRes);
    }

    public void exitChatRoom(Long memberId, Long chatRoomId) {
        Member member = entityFacade.getMember(memberId);

        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoom.getId(), memberId)
                .orElseThrow(() -> new RuntimeException("채팅방 참가자를 찾을 수 없습니다"));

        chatRoomMember.updateLastEntryTime();

        redisChatUtil.exitChatRoom(chatRoom.getId(), member.getId());
    }
}
