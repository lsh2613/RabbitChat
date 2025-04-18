package com.rabbitmqprac.chatroommember.dto;

import com.rabbitmqprac.chatroommember.ChatRoomMember;
import com.rabbitmqprac.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomMemberRes {
    private Long memberId;
    private String nickname;

    public static ChatRoomMemberRes of(ChatRoomMember chatRoomMember) {
        Member member = chatRoomMember.getMember();
        ChatRoomMemberRes memberRes = new ChatRoomMemberRes();
        memberRes.memberId = member.getId();
        memberRes.nickname = member.getNickname();
        return memberRes;
    }
}
