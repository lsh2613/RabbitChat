package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.api.OauthApi;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignInReq;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignUpReq;
import com.rabbitmqprac.application.dto.user.res.UserIdRes;
import com.rabbitmqprac.domain.context.oauth.service.OauthService;
import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.global.util.AuthenticationResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthController implements OauthApi {
    private final OauthService oauthService;

    @Override
    @PreAuthorize("isAnonymous()")
    @PostMapping("/oauth/sign-in")
    public ResponseEntity<UserIdRes> signIn(@RequestParam OauthProvider oauthProvider, @RequestBody @Validated OauthSignInReq req) {
        return AuthenticationResponseUtil.createAuthenticatedResponse(oauthService.signIn(oauthProvider, req));
    }

    @Override
    @PreAuthorize("isAnonymous()")
    @PostMapping("/oauth/sign-up")
    public ResponseEntity<UserIdRes> signUp(@RequestParam OauthProvider oauthProvider, @RequestBody @Validated OauthSignUpReq req) {
        return AuthenticationResponseUtil.createAuthenticatedResponse(oauthService.signUp(oauthProvider, req));
    }
}
