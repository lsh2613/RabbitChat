package com.rabbitmqprac.user;

import com.rabbitmqprac.global.annotation.Mapper;
import com.rabbitmqprac.user.dto.UserDetailRes;

@Mapper
public final class UserMapper {
    public static UserDetailRes toDetailRes(User user) {
        return UserDetailRes.of(user.getId(), user.getUsername());
    }
}
