package com.rabbitmqprac.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmqprac.global.exception.payload.ErrorResponse;
import com.rabbitmqprac.security.exception.JwtErrorException;
import com.rabbitmqprac.util.JwtErrorCodeUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("Exception caught in JwtExceptionFilter: {}", e.getMessage());
            JwtErrorException exception = JwtErrorCodeUtil.determineAuthErrorException(e);

            sendAuthError(response, exception);
        }
    }

    private void sendAuthError(HttpServletResponse response, JwtErrorException e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(e.getErrorCode().getStatusCode().getCode());

        ErrorResponse errorResponse = ErrorResponse.of(e.causedBy().getCode(), e.causedBy().getReason());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
