package com.rabbitmqprac.config;

import com.rabbitmqprac.infra.security.constant.WebSecurityUrls;
import com.rabbitmqprac.infra.security.filter.JwtAuthenticationFilter;
import com.rabbitmqprac.infra.security.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Value("${management.actuator.username}")
    private String actuatorUsername;
    @Value("${management.actuator.password}")
    private String actuatorPassword;
    @Value("${management.actuator.role}")
    private String actuatorRole;
    @Value("${management.endpoints.web.exposure.base-path}")
    private String metricsPath;

    @Bean(name = "actuatorUserDetailsService")
    public UserDetailsService actuatorUserDetailsService() {
        String encodedPassword = bCryptPasswordEncoder.encode(actuatorPassword);
        UserDetails actuatorUser = User.builder()
                .username(actuatorUsername)
                .password(encodedPassword)
                .roles(actuatorRole)
                .build();
        return new InMemoryUserDetailsManager(actuatorUser);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(
            HttpSecurity http,
            @Qualifier("actuatorUserDetailsService") UserDetailsService actuatorUserDetailsService
    ) throws Exception {
        http
                .securityMatcher(metricsPath)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole(actuatorRole)
                )
                .userDetailsService(actuatorUserDetailsService)
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        return defaultSecurity(http)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> defaultAuthorizeHttpRequests(auth)
                                .requestMatchers(metricsPath).permitAll()
                                .requestMatchers(WebSecurityUrls.SWAGGER_ENDPOINTS).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
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
                .requestMatchers(HttpMethod.GET, WebSecurityUrls.READ_ONLY_PUBLIC_ENDPOINTS).permitAll()
//                .requestMatchers(PUBLIC_ENDPOINTS).permitAll() //TODO 권한 필요한 API 추가 후 주석 해제
//                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .requestMatchers(WebSecurityUrls.PUBLIC_STOMP_ENDPOINTS).permitAll()
                .requestMatchers(WebSecurityUrls.ANONYMOUS_ENDPOINTS).anonymous();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
