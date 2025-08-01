package com.rabbitmqprac.application.dto.auth.req;

import com.rabbitmqprac.global.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;

public record AuthSignUpReq(
        @NotBlank(message = "아이디를 입력해주세요")
        @Pattern(regexp = "^[a-z0-9-_.]{5,20}$", message = "영문 소문자, 숫자, 특수기호 (-), (_), (.) 만 사용하여, 5~20자의 아이디를 입력해 주세요")
        String username,
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String password,
        @NotBlank(message = "확인 비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String confirmPassword
) {
    public String getEncodedPassword(PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.encode(password);
    }
}

