package com.rabbitmqprac.global.util;


import com.rabbitmqprac.global.annotation.Util;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Util
public final class OauthRequestBodyUtil {
    private static final String GRANT_TYPE = "authorization_code";

    public static MultiValueMap<String, String> createIdTokenRequestBody(
            String code,
            String clientId,
            String clientSecret,
            String redirectUri
    ) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        return formData;
    }
}
