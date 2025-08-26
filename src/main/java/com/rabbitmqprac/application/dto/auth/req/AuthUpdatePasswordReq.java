package com.rabbitmqprac.application.dto.auth.req;

import com.rabbitmqprac.global.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

@Schema(name = "authUpdatePasswordReq", title = "비밀번호 변경 요청 DTO")
public record AuthUpdatePasswordReq(
        @Schema(title = "기존 비밀번호", example = "oldPassword123!")
        @NotBlank(message = "비밀번호를 입력해주세요")
        String oldPassword,
        @Schema(title = "새로운 비밀번호", example = "NewPassword123!")
        @NotBlank(message = "새로운 비밀번호를 입력해주세요")
        @Password(message = "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요. (적어도 하나의 영문 소문자, 숫자 포함)")
        String newPassword
) {
    public String newPassword(PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.encode(newPassword);
    }
}
