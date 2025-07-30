package com.rabbitmqprac.user.dto;

public record UserDetailRes(
        Long userId,
        String nickname
) {
    public static UserDetailRes of(Long memberId, String nickname) {
        return new UserDetailRes(memberId, nickname);
    }
}