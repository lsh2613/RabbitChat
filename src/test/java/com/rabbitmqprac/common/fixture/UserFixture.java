package com.rabbitmqprac.common.fixture;

import com.rabbitmqprac.domain.persistence.user.entity.Role;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserFixture {
    FIRST_USER(1L, "user_1", "password_1", Role.USER),
    SECOND_USER(2L, "user_2", "password_2", Role.USER),
    ;

    private final Long id;
    private final String username;
    private final String password;
    private final Role role;

    public User toEntity() {
        return User.of(username, password, role);
    }
}