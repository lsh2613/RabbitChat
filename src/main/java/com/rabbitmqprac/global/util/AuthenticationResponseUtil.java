package com.rabbitmqprac.global.util;

import com.rabbitmqprac.application.dto.user.res.UserIdRes;
import com.rabbitmqprac.global.annotation.Util;
import com.rabbitmqprac.infra.security.jwt.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

@Util
public final class AuthenticationResponseUtil {
    public static ResponseEntity<UserIdRes> createAuthenticatedResponse(Pair<Long, Jwts> userInfo) {
        ResponseCookie cookie = CookieUtil.createCookie(
                "refreshToken", userInfo.getValue().refreshToken(), Duration.ofDays(7).toSeconds()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, userInfo.getValue().accessToken())
                .body(
                        UserIdRes.of(userInfo.getKey())
                );
    }
}
