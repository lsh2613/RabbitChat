package com.rabbitmqprac.common.annotation;

import com.rabbitmqprac.config.RedisConfig;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RedisConfig.class})
@ActiveProfiles("test")
@DataRedisTest
public @interface CustomRedisRepositoryTest {
}
