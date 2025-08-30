package com.rabbitmqprac.domain.context.user.dto.req;

import com.rabbitmqprac.domain.persistence.user.entity.Role;
import com.rabbitmqprac.domain.persistence.user.entity.User;

public record UserCreateReq(
        String nickname,
        String username,
        String password
) {
    public static UserCreateReq of(String nickname, String username, String password) {
        return new UserCreateReq(nickname, username, password);
    }

    public User toEntity() {
        return User.of(
                nickname,
                username,
                password,
                Role.USER
        );
    }
}
