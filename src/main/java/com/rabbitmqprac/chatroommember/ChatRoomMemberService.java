package com.rabbitmqprac.chatroommember;

import com.rabbitmqprac.chatroom.ChatRoom;
import com.rabbitmqprac.common.EntityFacade;
import com.rabbitmqprac.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomMemberService {
    private final EntityFacade entityFacade;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public void addChatRoomMember(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = entityFacade.getChatRoom(chatRoomId);
        Member member = entityFacade.getMember(memberId);

        ChatRoomMember chatRoomMember = ChatRoomMember.create(chatRoom, member);
        chatRoomMemberRepository.save(chatRoomMember);
    }
}
