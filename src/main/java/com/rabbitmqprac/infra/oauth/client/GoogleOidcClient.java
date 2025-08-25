package com.rabbitmqprac.infra.oauth.client;

import com.rabbitmqprac.infra.oauth.dto.OauthTokenRes;
import com.rabbitmqprac.infra.oauth.dto.OidcPublicKeyRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class GoogleOidcClient implements OauthOidcClient {
    private final WebClient webClient;

    @Value("${oauth2.client.provider.google.jwks-uri}")
    private String jwksUri;
    @Value("${oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Override
    public OidcPublicKeyRes getOidcPublicKey() {
        return webClient.get()
                .uri(jwksUri)
                .retrieve()
                .bodyToMono(OidcPublicKeyRes.class)
                .block();
    }

    @Override
    public OauthTokenRes getIdToken(MultiValueMap<String, ?> body) {
        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OauthTokenRes.class)
                .block();
    }
}
