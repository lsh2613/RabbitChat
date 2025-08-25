package com.rabbitmqprac.infra.oauth.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2.client.provider.google")
public class GoogleOidcProperties implements OauthOidcClientProperties {
    private final String clientId;
    private final String clientSecret;
    private final String issuer;
    private final String jwksUri;
    private final String nonce;
    private final String redirectUri;
}
