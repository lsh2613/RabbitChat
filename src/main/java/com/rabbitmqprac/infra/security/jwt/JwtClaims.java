package com.rabbitmqprac.infra.security.jwt;

import java.util.Map;

public interface JwtClaims {
    Map<String, ?> getClaims();
}
