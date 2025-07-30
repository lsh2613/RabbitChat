package com.rabbitmqprac.infra.security.constant;

public final class WebSecurityUrls {
    public static final String[] READ_ONLY_PUBLIC_ENDPOINTS = {"/favicon.ico", "/v1/duplicate/**"};
    public static final String[] PUBLIC_ENDPOINTS = {"/"};
    public static final String[] ANONYMOUS_ENDPOINTS = {"/auth/sign-in/**"};
    public static final String[] AUTHENTICATED_ENDPOINTS = {"/"};
    public static final String[] SWAGGER_ENDPOINTS = {"/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger",};
}
