package com.rabbitmqprac.infra.oauth.dto;

public record OidcPublicKey(
        String kid,
        String kty,
        String alg,
        String use,
        String n,
        String e
) {
}
