package com.rabbitmqprac.application.mapper;

import com.rabbitmqprac.application.dto.auth.res.UserDetailRes;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.annotation.Mapper;

@Mapper
public final class UserMapper {
    public static UserDetailRes toDetailRes(User user) {
        return UserDetailRes.of(user.getId(), user.getUsername());
    }
}
