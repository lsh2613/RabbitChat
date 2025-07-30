package com.rabbitmqprac.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.global.exception.payload.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("handle error: {}", accessDeniedException.getMessage());
        CausedBy causedBy = CausedBy.of(StatusCode.FORBIDDEN, ReasonCode.ACCESS_TO_THE_REQUESTED_RESOURCE_IS_FORBIDDEN, DomainCode.JWT);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(causedBy.statusCode().getCode());
        ErrorResponse errorResponse = ErrorResponse.of(causedBy.getCode(), causedBy.getReason());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}