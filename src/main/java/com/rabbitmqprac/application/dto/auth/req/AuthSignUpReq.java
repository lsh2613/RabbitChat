package com.rabbitmqprac.application.dto.auth.req;

import com.rabbitmqprac.global.annotation.Nickname;
import com.rabbitmqprac.global.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;

@Schema(name = "authSignUpReq", title = "일반 회원가입 요청 DTO")
public record AuthSignUpReq(
        @Schema(title = "닉네임", example = "스프링")
        @NotBlank(message = "닉네임을 입력해주세요")
        @Nickname
        String nickname,
        @Schema(title = "아이디", example = "rabbit")
        @NotBlank(message = "아이디를 입력해주세요")
        @Pattern(regexp = "^[a-z0-9-_.]{5,20}$", message = "영문 소문자, 숫자만 사용하여, 5~20자의 아이디를 입력해 주세요")
        String username,
        @Schema(title = "비밀번호", example = "rabbit1234")
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String password,
        @Schema(title = "확인 비밀번호", example = "rabbit1234")
        @NotBlank(message = "확인 비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String confirmPassword
) {
    public String getEncodedPassword(PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.encode(password);
    }
}
