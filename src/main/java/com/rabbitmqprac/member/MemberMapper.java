package com.rabbitmqprac.member;

import com.rabbitmqprac.global.annotation.Mapper;
import com.rabbitmqprac.member.dto.MemberDetailRes;

@Mapper
public final class MemberMapper {
    public static MemberDetailRes toDetailRes(Member member) {
        return MemberDetailRes.of(member.getId(), member.getNickname());
    }
}
