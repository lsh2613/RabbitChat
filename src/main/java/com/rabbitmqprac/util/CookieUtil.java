package com.rabbitmqprac.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor
public final class CookieUtil {

    public static Optional<Cookie> getCookieFromRequest(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findAny();
    }

    public static ResponseCookie createCookie(String cookieName, String value, long maxAge) {
        return ResponseCookie.from(cookieName, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public static ResponseCookie deleteCookie(String cookieName) {
        return createCookie(cookieName, "", 0);
    }
}

