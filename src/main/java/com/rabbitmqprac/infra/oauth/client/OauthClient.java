package com.rabbitmqprac.infra.oauth.client;

import com.rabbitmqprac.infra.oauth.dto.OauthTokenRes;
import org.springframework.util.MultiValueMap;

public interface OauthClient {
    OauthTokenRes getIdToken(MultiValueMap<String, ?> body);
}
