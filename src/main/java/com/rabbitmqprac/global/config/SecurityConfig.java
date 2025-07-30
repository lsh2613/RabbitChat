package com.rabbitmqprac.global.config;

import com.rabbitmqprac.global.filter.JwtAuthenticationFilter;
import com.rabbitmqprac.global.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.rabbitmqprac.global.constant.WebSecurityUrls.*;

@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
        log.info("filterChainDev 등록");
        return defaultSecurity(http)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> defaultAuthorizeHttpRequests(auth)
                                .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                                .anyRequest().authenticated()
                ).build();
    }

    private HttpSecurity defaultSecurity(HttpSecurity http) throws Exception {
        return http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        exception -> exception
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)
                );
    }

    private AbstractRequestMatcherRegistry<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl> defaultAuthorizeHttpRequests(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        return auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "*").permitAll()
                .requestMatchers(HttpMethod.GET, READ_ONLY_PUBLIC_ENDPOINTS).permitAll()
//                .requestMatchers(PUBLIC_ENDPOINTS).permitAll() //TODO 권한 필요한 API 추가 후 주석 해제
//                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .requestMatchers(ANONYMOUS_ENDPOINTS).anonymous();
    }
}
