package com.rabbitmqprac.global.helper;

import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.global.annotation.AccessTokenStrategy;
import com.rabbitmqprac.global.annotation.Helper;
import com.rabbitmqprac.global.annotation.RefreshTokenStrategy;
import com.rabbitmqprac.global.util.JwtClaimsParserUtil;
import com.rabbitmqprac.infra.security.jwt.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Helper
public class JwtHelper {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;

    public JwtHelper(
            @AccessTokenStrategy JwtProvider accessTokenProvider,
            @RefreshTokenStrategy JwtProvider refreshTokenProvider
    ) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    /**
     * 사용자 정보 기반으로 access token과 refresh token을 생성하는 메서드 <br/>
     * refresh token은 redis에 저장된다.
     *
     * @param user {@link User} : 사용자 정보
     * @return {@link Jwts}
     */
    public Jwts createToken(User user) {
        String accessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(user.getId(), user.getRole().getType()));
        String refreshToken = refreshTokenProvider.generateToken(RefreshTokenClaim.of(user.getId(), user.getRole().getType()));

        return Jwts.of(accessToken, refreshToken);
    }

    public Jwts refresh(String refreshToken) {
        JwtClaims claims = refreshTokenProvider.getJwtClaimsFromToken(refreshToken);

        Long userId = JwtClaimsParserUtil.getClaimsValue(claims, RefreshTokenClaimKeys.USER_ID.getValue(), Long::parseLong);
        String role = JwtClaimsParserUtil.getClaimsValue(claims, RefreshTokenClaimKeys.ROLE.getValue(), String.class);
        log.debug("refresh token userId : {}, role : {}", userId, role);

        String newAccessToken = accessTokenProvider.generateToken(AccessTokenClaim.of(userId, role));
        String newRefreshToken = refreshTokenProvider.generateToken(RefreshTokenClaim.of(userId, role));
        log.debug("new access token : {}", newAccessToken);
        log.debug("new refresh token : {}", newRefreshToken);

        return Jwts.of(newAccessToken, newRefreshToken);
    }

}
