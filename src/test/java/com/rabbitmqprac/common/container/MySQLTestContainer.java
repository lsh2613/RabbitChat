package com.rabbitmqprac.common.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MySQLTestContainer {
    private static final String MYSQL_CONTAINER_IMAGE = "mysql:8.0.36";

    private static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER =
                new MySQLContainer<>(DockerImageName.parse(MYSQL_CONTAINER_IMAGE))
                        .withDatabaseName("rabbitmq-prac")
                        .withUsername("root")
                        .withPassword("1234")
                        .withCommand("--sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION")
                        .withReuse(true);

        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void setRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&characterEncoding=utf8",
                        MYSQL_CONTAINER.getHost(),
                        MYSQL_CONTAINER.getMappedPort(3306),
                        MYSQL_CONTAINER.getDatabaseName()
                )
        );
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);

        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }
}
