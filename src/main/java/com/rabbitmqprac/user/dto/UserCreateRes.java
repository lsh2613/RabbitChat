package com.rabbitmqprac.user.dto;

import com.rabbitmqprac.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRes {
    private Long memberId;
    private String nickname;
    private String accessToken;

    public static UserCreateRes createRes(User user, String accessToken) {
        UserCreateRes userCreateRes = new UserCreateRes();
        userCreateRes.memberId = user.getId();
        userCreateRes.nickname = user.getUsername();
        userCreateRes.accessToken = accessToken;
        return userCreateRes;
    }
}
