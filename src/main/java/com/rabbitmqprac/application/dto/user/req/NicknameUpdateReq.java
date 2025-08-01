package com.rabbitmqprac.application.dto.user.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NicknameUpdateReq(
        @NotBlank(message = "닉네임을 입력해주세요")
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,8}$", message = "한글과 영문 대, 소문자만 가능해요")
        String nickname
) {
}
