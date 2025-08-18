package com.rabbitmqprac.application.dto.auth.res;

public record UserDetailRes(
        Long userId,
        String nickname
) {
    public static UserDetailRes of(Long memberId, String nickname) {
        return new UserDetailRes(memberId, nickname);
    }
}
