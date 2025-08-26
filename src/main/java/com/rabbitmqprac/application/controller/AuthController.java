package com.rabbitmqprac.application.controller;

import com.rabbitmqprac.application.api.AuthApi;
import com.rabbitmqprac.application.dto.auth.req.AuthSignInReq;
import com.rabbitmqprac.application.dto.auth.req.AuthSignUpReq;
import com.rabbitmqprac.application.dto.auth.req.AuthUpdatePasswordReq;
import com.rabbitmqprac.application.dto.user.res.UserIdRes;
import com.rabbitmqprac.domain.context.auth.service.AuthService;
import com.rabbitmqprac.global.util.AuthenticationResponseUtil;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<UserIdRes> signUp(@RequestBody @Validated AuthSignUpReq authSignUpReq) {
        return AuthenticationResponseUtil.createAuthenticatedResponse(authService.signUp(authSignUpReq));
    }

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<UserIdRes> signIn(@RequestBody @Validated AuthSignInReq authSignInReq) {
        return AuthenticationResponseUtil.createAuthenticatedResponse(authService.signIn(authSignInReq));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    @PatchMapping("/password")
    public void patchPassword(@AuthenticationPrincipal SecurityUserDetails user, @RequestBody @Validated AuthUpdatePasswordReq authUpdatePasswordReq) {
        authService.updatePassword(user.getUserId(), authUpdatePasswordReq);
    }


}
