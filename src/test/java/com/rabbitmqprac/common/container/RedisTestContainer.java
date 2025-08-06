package com.rabbitmqprac.common.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class RedisTestContainer {
    private static final String REDIS_CONTAINER_NAME = "redis:7.4";
    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_CONTAINER_NAME))
                        .withExposedPorts(6379)
                        .withReuse(true);

        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void setRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> String.valueOf(REDIS_CONTAINER.getMappedPort(6379)));
    }
}
