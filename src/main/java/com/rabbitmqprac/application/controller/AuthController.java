package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.dto.auth.req.AuthSignInReq;
import com.rabbitmqprac.application.dto.auth.req.AuthSignUpReq;
import com.rabbitmqprac.application.dto.auth.req.AuthUpdatePasswordReq;
import com.rabbitmqprac.domain.context.auth.service.AuthService;
import com.rabbitmqprac.global.util.CookieUtil;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Validated AuthSignUpReq authSignUpReq) {
        return createAuthenticatedResponse(authService.signUp(authSignUpReq));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Validated AuthSignInReq authSignInReq) {
        return createAuthenticatedResponse(authService.signIn(authSignInReq));
    }

    @PatchMapping("/password")
    public ResponseEntity<?> patchPassword(@AuthenticationPrincipal SecurityUserDetails user, @RequestBody @Validated AuthUpdatePasswordReq authUpdatePasswordReq) {
        authService.updatePassword(user.getUserId(), authUpdatePasswordReq);
        return ResponseEntity.noContent().build();
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
