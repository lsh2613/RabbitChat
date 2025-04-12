package com.rabbitmqprac.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRes {
    private Long memberId;
    private String username;
    private String accessToken;

    public static MemberRes createRes(Member member, String accessToken) {
        MemberRes memberRes = new MemberRes();
        memberRes.memberId = member.getId();
        memberRes.username = member.getUsername();
        memberRes.accessToken = accessToken;
        return memberRes;
    }
}
