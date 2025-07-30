package com.rabbitmqprac.domain.context.auth.service;

import com.rabbitmqprac.infra.security.jwt.Jwts;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtHelper jwtHelper;
    private final UserService userService;

    public Pair<Long, Jwts> signIn(Long userId) {
        User user = userService.readUser(userId);

        return Pair.of(userId, jwtHelper.createToken(user));
    }
}
