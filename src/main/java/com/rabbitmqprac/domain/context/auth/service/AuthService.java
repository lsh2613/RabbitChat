package com.rabbitmqprac.domain.context.auth.service;

import com.rabbitmqprac.application.dto.auth.req.AuthSignInReq;
import com.rabbitmqprac.application.dto.auth.req.AuthSignUpReq;
import com.rabbitmqprac.application.dto.auth.req.AuthUpdatePasswordReq;
import com.rabbitmqprac.domain.context.auth.exception.AuthErrorCode;
import com.rabbitmqprac.domain.context.auth.exception.AuthErrorException;
import com.rabbitmqprac.domain.context.user.dto.req.UserCreateReq;
import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.helper.JwtHelper;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Pair<Long, Jwts> signUp(AuthSignUpReq req) {
        if (!isSamePassword(req))
            throw new AuthErrorException(AuthErrorCode.PASSWORD_CONFIRM_MISMATCH);

        User user = userService.saveUserWithEncryptedPassword(
                UserCreateReq.of(req.username(), req.getEncodedPassword(bCryptPasswordEncoder))
        );

        return Pair.of(user.getId(), jwtHelper.createToken(user));
    }

    private static boolean isSamePassword(AuthSignUpReq req) {
        return req.password().equals(req.confirmPassword());
    }

    @Transactional(readOnly = true)
    public Pair<Long, Jwts> signIn(AuthSignInReq req) {
        User user = userService.readUserByUsername(req.username());
        String password = req.password();

        if (!isValidPassword(password, user))
            throw new AuthErrorException(AuthErrorCode.INVALID_PASSWORD);

        return Pair.of(user.getId(), jwtHelper.createToken(user));
    }

    @Transactional
    public void updatePassword(Long userId, AuthUpdatePasswordReq req) {
        User user = userService.readUser(userId);

        if (!isValidPassword(req.oldPassword(), user))
            throw new AuthErrorException(AuthErrorCode.INVALID_PASSWORD);

        String newPassword = req.newPassword(bCryptPasswordEncoder);
        user.updatePassword(newPassword);
    }

    private boolean isValidPassword(String password, User user) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }
}
