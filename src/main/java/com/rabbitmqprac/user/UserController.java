package com.rabbitmqprac.user;

import com.rabbitmqprac.user.dto.UserDetailRes;
import com.rabbitmqprac.security.jwt.AccessTokenClaim;
import com.rabbitmqprac.security.jwt.RefreshTokenClaim;
import com.rabbitmqprac.security.authentication.SecurityUserDetails;
import com.rabbitmqprac.util.AccessTokenProvider;
import com.rabbitmqprac.util.CookieUtil;
import com.rabbitmqprac.util.RefreshTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    // todo AuthController로 이동
    @GetMapping("/login/{userId}")
    public ResponseEntity<?> login(@PathVariable("userId") Long userId) {
        User user = userService.readById(userId);

        String accessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(user.getId(), user.getRole().getType()));
        String refreshToken = refreshTokenProvider.generateToken(RefreshTokenClaim.of(user.getId(), user.getRole().getType()));

        log.info("accessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

        ResponseCookie cookie = CookieUtil.createCookie("refreshToken", refreshToken, Duration.ofDays(7).toSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("userId", userId));
    }

    @GetMapping("/users/me")
    public UserDetailRes getMember(@AuthenticationPrincipal SecurityUserDetails user) {
        return userService.getUserDetail(user.getUserId());
    }

    @GetMapping("/users")
    public List<User> getMembers() {
        return userService.getUsers();
    }
}
