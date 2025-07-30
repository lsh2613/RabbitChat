package com.rabbitmqprac.security.jwt;

import java.util.Map;

public interface JwtClaims {
    Map<String, ?> getClaims();
}
