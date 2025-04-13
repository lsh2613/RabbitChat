package com.rabbitmqprac.member.dto;

import com.rabbitmqprac.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRes {
    private Long memberId;
    private String username;
    private String accessToken;

    public static MemberCreateRes createRes(Member member, String accessToken) {
        MemberCreateRes memberCreateRes = new MemberCreateRes();
        memberCreateRes.memberId = member.getId();
        memberCreateRes.username = member.getUsername();
        memberCreateRes.accessToken = accessToken;
        return memberCreateRes;
    }
}
