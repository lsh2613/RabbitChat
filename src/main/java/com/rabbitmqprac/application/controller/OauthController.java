package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.api.OauthApi;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignInReq;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignUpReq;
import com.rabbitmqprac.domain.context.oauth.service.OauthService;
import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.global.util.CookieUtil;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class OauthController implements OauthApi {
    private final OauthService oauthService;

    @Override
    @PreAuthorize("isAnonymous()")
    @PostMapping("/oauth/sign-in")
    public ResponseEntity<?> signIn(@RequestParam OauthProvider oauthProvider, @RequestBody @Validated OauthSignInReq req) {
        return createAuthenticatedResponse(oauthService.signIn(oauthProvider, req));
    }

    @Override
    @PreAuthorize("isAnonymous()")
    @PostMapping("/oauth/sign-up")
    public ResponseEntity<?> signUp(@RequestParam OauthProvider oauthProvider, @RequestBody @Validated OauthSignUpReq req) {
        return createAuthenticatedResponse(oauthService.signUp(oauthProvider, req));
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
