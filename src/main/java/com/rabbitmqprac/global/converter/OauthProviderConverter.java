package com.rabbitmqprac.global.converter;

import com.rabbitmqprac.domain.persistence.oauth.constant.OauthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class OauthProviderConverter implements Converter<String, OauthProvider> {
    @Override
    public OauthProvider convert(String provider) {
        try {
            return OauthProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MethodArgumentTypeMismatchException(
                    provider,
                    OauthProvider.class,
                    "oauthProvider",
                    null,
                    e
            );
        }
    }
}
