package com.rabbitmqprac.config;

import com.rabbitmqprac.global.interceptor.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final Optional<RequestInterceptor> requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (requestInterceptor.isPresent()) {
            registry.addInterceptor(requestInterceptor.get());
        }
    }

}
