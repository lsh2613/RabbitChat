package com.rabbitmqprac.chatroom;


import com.rabbitmqprac.chatmessage.ChatMessage;
import com.rabbitmqprac.chatmessage.ChatMessageRepository;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.chatroommember.ChatRoomMemberRepository;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.common.dto.ChatRoomRes;
import com.rabbitmqprac.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateReq;
import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateRes;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final EntityFacade entityFacade;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public ChatRoomCreateRes createChatRoomForPersonal(Long loginId, ChatRoomCreateReq req) {
        Member member = entityFacade.getMember(loginId);
        Member counterpart = entityFacade.getMember(req.getCounterpartId());

        ChatRoom newRoom = ChatRoom.create();
        newRoom.addChatRoomMember(new ChatRoomMember(newRoom, member));
        newRoom.addChatRoomMember(new ChatRoomMember(newRoom, counterpart));
        chatRoomRepository.save(newRoom);

        return ChatRoomCreateRes.createRes(newRoom.getId(), member.getId(), counterpart.getId());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomRes> getChatRooms(Long memberId) {
        Member member = entityFacade.getMember(memberId);

        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByMemberId(member.getId());

        List<ChatRoomRes> chatRoomResList = chatRoomMembers.stream().map(chatroomMember -> {
                    ChatRoom chatRoom = chatroomMember.getChatRoom();

                    Optional<ChatMessage> lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId());
                    int unreadMessageCnt = chatMessageRepository.countByChatRoomIdAndCreatedAtAfter(chatRoom.getId(), chatroomMember.getLastEntryTime());

                    return ChatRoomRes.createRes(chatRoom.getId(), member.getUsername(), unreadMessageCnt, lastMessage);
                })
                .toList();

        return chatRoomResList;
    }
}
