package com.rabbitmqprac.global.interceptor;

import com.rabbitmqprac.global.constant.SessionType;
import com.rabbitmqprac.global.util.RequestInfoExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.UUID;

@Profile("prod")
@Slf4j
@RequiredArgsConstructor
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute(SessionType.REQUEST_ID, requestId);
        request.setAttribute(SessionType.START_TIME, System.currentTimeMillis());
        logPreRequest(requestId, request.getRequestURI(), request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long turnaroundTimeSec = getTurnaroundTimeSec(request);

        completeRequest(response.getStatus(), turnaroundTimeSec, ex);
    }

    private void logPreRequest(String requestId, String requestURI, String requestMethod) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (authentication == null || Objects.equals(authentication.getName(), "anonymousUser")) ? "anonymous" : authentication.getName();
        String clientIp = RequestInfoExtractor.getClientIpAddressIfServletRequestExist();

        MDC.put("requestId", requestId);
        MDC.put("userId", userId);
        MDC.put("clientIp", clientIp);
        MDC.put("requestURI", requestURI);
        MDC.put("requestMethod", requestMethod);

        log.info("HTTP request started");
    }

    private void completeRequest(int status, long turnaroundTime, Exception ex) {
        MDC.put("httpStatus", String.valueOf(status));
        MDC.put("turnaroundTime(sec)", String.valueOf(turnaroundTime));

        if (ex == null) {
            log.info("HTTP request completed");
        } else {
            log.error("HTTP request failed", ex);
        }

        MDC.clear();
    }

    private static long getTurnaroundTimeSec(HttpServletRequest request) {
        long startTime = (Long) request.getAttribute(SessionType.START_TIME);
        long turnaroundTimeSec = (System.currentTimeMillis() - startTime) / 1000;
        return turnaroundTimeSec;
    }

}
