package com.rabbitmqprac.application.dto.auth.req;

import com.rabbitmqprac.global.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

public record AuthUpdatePasswordReq(
        @NotBlank(message = "비밀번호를 입력해주세요")
        String oldPassword,
        @NotBlank(message = "새로운 비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String newPassword
) {
        public String newPassword(PasswordEncoder bCryptPasswordEncoder) {
                return bCryptPasswordEncoder.encode(newPassword);
        }
}
