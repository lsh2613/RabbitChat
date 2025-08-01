package com.rabbitmqprac.domain.context.user.dto.req;

import com.rabbitmqprac.domain.persistence.user.entity.Role;
import com.rabbitmqprac.domain.persistence.user.entity.User;

public record UserCreateReq(
        String username,
        String password
) {
    public static UserCreateReq of(String username, String password) {
        return new UserCreateReq(username, password);
    }

    public User toEntity() {
        return User.of(
                username,
                password,
                Role.USER
        );
    }
}
