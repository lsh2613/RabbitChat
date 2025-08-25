package com.rabbitmqprac.config;

import com.rabbitmqprac.infra.oauth.properties.GoogleOidcProperties;
import com.rabbitmqprac.infra.oauth.properties.KakaoOidcProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ServerProperties.class,
        GoogleOidcProperties.class,
        KakaoOidcProperties.class
})
public class OauthConfig {
}
