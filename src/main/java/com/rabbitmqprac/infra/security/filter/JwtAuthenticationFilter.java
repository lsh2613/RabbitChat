package com.rabbitmqprac.infra.security.filter;

import com.rabbitmqprac.infra.security.exception.JwtErrorCode;
import com.rabbitmqprac.infra.security.exception.JwtErrorException;
import com.rabbitmqprac.infra.security.jwt.AccessTokenClaimKeys;
import com.rabbitmqprac.infra.security.jwt.JwtClaims;
import com.rabbitmqprac.infra.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailService;
    private final JwtProvider accessTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isAnonymousRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(request);

        UserDetails userDetails = getUserDetails(accessToken);
        authenticateUser(userDetails, request);
        filterChain.doFilter(request, response);
    }

    /**
     * AccessToken과 RefreshToken이 모두 없는 경우, 익명 사용자로 간주한다.
     */
    private boolean isAnonymousRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        return accessToken == null;
    }

    /**
     * @throws ServletException : Authorization 헤더가 없거나, 금지된 토큰이거나, 토큰이 만료된 경우 예외 발생
     */
    private String resolveAccessToken(HttpServletRequest request) throws ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = accessTokenProvider.resolveToken(authHeader);

        if (!StringUtils.hasText(token)) {
            handleAuthException(JwtErrorCode.EMPTY_ACCESS_TOKEN);
        }

        if (accessTokenProvider.isTokenExpired(token)) {
            handleAuthException(JwtErrorCode.EXPIRED_TOKEN);
        }

        return token;
    }

    private UserDetails getUserDetails(String accessToken) {
        JwtClaims claims = accessTokenProvider.getJwtClaimsFromToken(accessToken);
        String userId = (String) claims.getClaims().get(AccessTokenClaimKeys.USER_ID.getValue());
        log.debug("User ID: {}", userId);

        return userDetailService.loadUserByUsername(userId);
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authenticated user: {}", userDetails.getUsername());
    }

    private void handleAuthException(JwtErrorCode errorCode) throws ServletException {
        log.warn("AuthErrorException(code={}, message={})", errorCode.name(), errorCode.getExplainError());
        JwtErrorException exception = new JwtErrorException(errorCode);
        throw new ServletException(exception);
    }
}
