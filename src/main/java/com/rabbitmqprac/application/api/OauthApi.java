package com.rabbitmqprac.application.api;

import com.rabbitmqprac.application.dto.oauth.req.OauthSignInReq;
import com.rabbitmqprac.application.dto.oauth.req.OauthSignUpReq;
import com.rabbitmqprac.domain.context.oauth.exception.OauthErrorCode;
import com.rabbitmqprac.domain.context.user.exception.UserErrorCode;
import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import com.rabbitmqprac.global.annotation.ApiExceptionExplanation;
import com.rabbitmqprac.global.annotation.ApiExceptionExplanations;
import com.rabbitmqprac.infra.security.exception.JwtErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "[소셜 인증 API]")
public interface OauthApi {

    @Operation(
            summary = "소셜 로그인",
            description = """
                    code는 각 소셜 플랫폼에서 발급받은 인가 코드로 KAKAO, GOOGLE 등의 OauthProvider를 통해 발급받은 인가 코드를 의미합니다.
                    - [KAKAO CODE 발급 URL](https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=57e328d97dc0c7b53898e2e79082f23c&redirect_uri=http://localhost:8080&scope=openid%20profile_nickname&nonce=example-nonce)
                    - [GOOGLE CODE 발급 URL](https://accounts.google.com/o/oauth2/v2/auth?client_id=248388975343-0oo0f79rrsqpf1k63ahpivkhd2rfu1jp.apps.googleusercontent.com&redirect_uri=http://localhost:8080&response_type=code&scope=openid%20email%20profile&nonce=example-nonce)
                    """
    )
    @ApiExceptionExplanations({
            @ApiExceptionExplanation(
                    errorCode = OauthErrorCode.class,
                    constants = {"MISSING_ISS", "INVALID_ISS", "MISSING_NONCE", "INVALID_ISS", "INVALID_AUD", "INVALID_NONCE"}
            ),
            @ApiExceptionExplanation(
                    errorCode = JwtErrorCode.class,
                    constants = "MALFORMED_TOKEN"
            )
    })
    ResponseEntity<?> signIn(
            @RequestParam OauthProvider oauthProvider,
            @RequestBody @Validated OauthSignInReq req
    );


    @Operation(
            summary = "소셜 회원가입",
            description = """
                    code는 각 소셜 플랫폼에서 발급받은 인가 코드로 KAKAO, GOOGLE 등의 OauthProvider를 통해 발급받은 인가 코드를 의미합니다.
                    - [KAKAO CODE 발급 URL](https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=57e328d97dc0c7b53898e2e79082f23c&redirect_uri=http://localhost:8080&scope=openid%20profile_nickname&nonce=example-nonce)
                    - [GOOGLE CODE 발급 URL](https://accounts.google.com/o/oauth2/v2/auth?client_id=248388975343-0oo0f79rrsqpf1k63ahpivkhd2rfu1jp.apps.googleusercontent.com&redirect_uri=http://localhost:8080&response_type=code&scope=openid%20email%20profile&nonce=example-nonce)
                    """
    )
    @ApiExceptionExplanations({
            @ApiExceptionExplanation(
                    errorCode = OauthErrorCode.class,
                    constants = {"CONFLICT", "MISSING_ISS", "INVALID_ISS", "MISSING_NONCE", "INVALID_ISS", "INVALID_AUD", "INVALID_NONCE"}
            ),
            @ApiExceptionExplanation(
                    errorCode = UserErrorCode.class,
                    constants = "CONFLICT_USERNAME"
            )
    })
    ResponseEntity<?> signUp(
            @RequestParam OauthProvider oauthProvider,
            @RequestBody @Validated OauthSignUpReq req
    );
}
