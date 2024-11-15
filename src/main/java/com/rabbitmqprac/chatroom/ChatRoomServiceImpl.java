package com.rabbitmqprac.chatroom;


import com.rabbitmqprac.chatmessage.ChatMessage;
import com.rabbitmqprac.chatmessage.ChatMessageRepository;
import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.chatroommember.ChatRoomMemberRepository;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.common.dto.ChatRoomRes;
import com.rabbitmqprac.user.Member;
import com.rabbitmqprac.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateReq;
import static com.rabbitmqprac.common.dto.ChatDto.ChatRoomCreateRes;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final EntityFacade entityFacade;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public ChatRoomCreateRes createChatRoomForPersonal(Long loginId, ChatRoomCreateReq chatRoomReq) {
        Member roomMaker = entityFacade.getMember(loginId);
        Member guest = entityFacade.getMember(loginId);

        ChatRoom newRoom = chatRoomReq.createChatRoom();
        newRoom.addChatRoomMember(new ChatRoomMember(newRoom, roomMaker));
        newRoom.addChatRoomMember(new ChatRoomMember(newRoom, guest));
        chatRoomRepository.save(newRoom);

        return ChatRoomCreateRes.createRes(newRoom.getId(), roomMaker.getId(), guest.getId());
    }

    @Override
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
