package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.auth.req.AuthSignInReq;
import com.rabbitmqprac.application.dto.auth.req.AuthSignUpReq;
import com.rabbitmqprac.application.dto.auth.req.AuthUpdatePasswordReq;
import com.rabbitmqprac.infra.security.authentication.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "[인증 API]")
public interface AuthApi {

    @Operation(summary = "일반 회원가입")
    ResponseEntity<Map<String, Long>> signUp(@RequestBody @Validated AuthSignUpReq authSignUpReq);

    @Operation(summary = "일반 로그인")
    ResponseEntity<Map<String, Long>> signIn(@RequestBody @Validated AuthSignInReq authSignInReq);

    @Operation(summary = "비밀번호 변경")
    ResponseEntity<?> patchPassword(@AuthenticationPrincipal SecurityUserDetails user, @RequestBody @Validated AuthUpdatePasswordReq authUpdatePasswordReq);
}
