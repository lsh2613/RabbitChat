package com.rabbitmqprac.common.config;

import com.rabbitmqprac.common.helper.EntitySaver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestBeanConfig {
    @Bean
    @ConditionalOnMissingBean
    public EntitySaver entitySaver() {
        return new EntitySaver();
    }
}
