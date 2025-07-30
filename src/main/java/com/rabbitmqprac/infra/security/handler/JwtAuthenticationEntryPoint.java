package com.rabbitmqprac.infra.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.global.exception.payload.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("commence error: {}", authException.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.UNAUTHORIZED, ReasonCode.MISSING_OR_INVALID_AUTHENTICATION_CREDENTIALS, DomainCode.JWT);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(causedBy.statusCode().getCode());
        ErrorResponse errorResponse = ErrorResponse.of(causedBy.getCode(), causedBy.getReason());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}