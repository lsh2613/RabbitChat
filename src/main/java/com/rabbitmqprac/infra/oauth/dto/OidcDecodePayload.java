package com.rabbitmqprac.infra.oauth.dto;

public record OidcDecodePayload(
        /* issuer */
        String iss,
        /* client id */
        String aud,
        /* aouth provider account unique id */
        String sub,
        String email
) {
}
