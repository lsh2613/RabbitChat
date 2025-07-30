package com.rabbitmqprac.member.dto;

public record MemberDetailRes(
        Long memberId,
        String nickname
) {
    public static MemberDetailRes of(Long memberId, String nickname) {
        return new MemberDetailRes(memberId, nickname);
    }
}