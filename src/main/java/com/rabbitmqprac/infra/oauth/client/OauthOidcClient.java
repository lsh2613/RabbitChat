package com.rabbitmqprac.infra.oauth.client;

import com.rabbitmqprac.infra.oauth.dto.OidcPublicKeyRes;

public interface OauthOidcClient extends OauthClient {
    OidcPublicKeyRes getOidcPublicKey();
}
