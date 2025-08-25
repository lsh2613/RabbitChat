package com.rabbitmqprac.infra.oauth.properties;

public interface OauthOidcClientProperties {
    String getJwksUri();

    String getClientId();

    String getIssuer();

    String getNonce();

    String getClientSecret();

    String getRedirectUri();
}
