package com.rabbitmqprac.application.dto.auth.req;

public record AuthSignInReq(
        String username,
        String password
) {
}
