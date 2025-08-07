package com.rabbitmqprac.infra.stomp.handler.command.connect;

import com.rabbitmqprac.domain.context.user.service.UserService;
import com.rabbitmqprac.domain.context.usersession.service.UserSessionService;
import com.rabbitmqprac.domain.persistence.user.entity.User;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserSession;
import com.rabbitmqprac.domain.persistence.usersession.entity.UserStatus;
import com.rabbitmqprac.global.helper.AccessTokenProvider;
import com.rabbitmqprac.global.util.JwtClaimsParserUtil;
import com.rabbitmqprac.infra.security.exception.JwtErrorCode;
import com.rabbitmqprac.infra.security.exception.JwtErrorException;
import com.rabbitmqprac.infra.security.jwt.AccessTokenClaimKeys;
import com.rabbitmqprac.infra.security.jwt.AuthConstants;
import com.rabbitmqprac.infra.security.jwt.JwtClaims;
import com.rabbitmqprac.infra.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectAuthenticateHandler implements ConnectCommandHandler {
    private final AccessTokenProvider accessTokenProvider;
    private final UserService userService;
    private final UserSessionService userSessionService;

    @Override
    public void handle(Message<?> message, StompHeaderAccessor accessor) {
        String accessToken = extractAccessToken(accessor);

        JwtClaims claims = accessTokenProvider.getJwtClaimsFromToken(accessToken);
        Long userId = JwtClaimsParserUtil.getClaimsValue(claims, AccessTokenClaimKeys.USER_ID.getValue(), Long::parseLong);
        LocalDateTime expiresDate = accessTokenProvider.getExpiryDate(accessToken);

        UserPrincipal principal = (UserPrincipal) authenticateUser(accessor, userId, expiresDate);
        activateUserSession(principal);
    }

    private String extractAccessToken(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader(AuthConstants.AUTHORIZATION.getValue());

        if ((authorization == null || !authorization.startsWith(AuthConstants.TOKEN_TYPE.getValue()))) {
            log.warn("[인증 핸들러] 헤더에 Authorization이 없거나 Bearer 토큰이 아닙니다.");
            throw new JwtErrorException(JwtErrorCode.EMPTY_ACCESS_TOKEN);
        }

        return authorization.substring(7);
    }

    private Principal authenticateUser(StompHeaderAccessor accessor, Long userId, LocalDateTime expiresDate) {
        User user = userService.readUser(userId);
        Principal principal = UserPrincipal.of(user, expiresDate);

        log.info("[인증 핸들러] 사용자 인증 완료: {}", principal);

        accessor.setUser(principal);

        return principal;
    }

    private void activateUserSession(UserPrincipal principal) {
        if (userSessionService.isExists(principal.getUserId())) {
            log.info("[인증 핸들러] 사용자 세션을 갱신합니다. userId: {}", principal.getUserId());
            userSessionService.updateUserStatus(principal.getUserId(), UserStatus.ACTIVE_APP);
            log.info("[인증 핸들러] 사용자 세션을 갱신했습니다. userId: {}", principal.getUserId());
        } else {
            log.info("[인증 핸들러] 사용자 세션을 생성합니다. userId: {}", principal.getUserId());
            userSessionService.create(principal.getUserId(), UserSession.of(principal.getUserId()));
            log.info("[인증 핸들러] 사용자 세션을 생성했습니다. userId: {}", principal.getUserId());
        }
    }
}
