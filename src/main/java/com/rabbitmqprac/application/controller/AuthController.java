package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.domain.context.auth.service.AuthService;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import com.rabbitmqprac.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @GetMapping("/sign-in/{userId}")
    public ResponseEntity<?> signIn(@PathVariable("userId") Long userId) {
        return createAuthenticatedResponse(authService.signIn(userId));
    }

    private ResponseEntity<?> createAuthenticatedResponse(Pair<Long, Jwts> userInfo) {
        ResponseCookie cookie = CookieUtil.createCookie(
                "refreshToken", userInfo.getValue().refreshToken(), Duration.ofDays(7).toSeconds()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, userInfo.getValue().accessToken())
                .body(
                        Map.of("userId", userInfo.getKey())
                );
    }

}
