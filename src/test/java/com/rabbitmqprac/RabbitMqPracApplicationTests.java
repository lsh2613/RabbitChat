package com.rabbitmqprac;

import com.rabbitmqprac.common.container.MySQLTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class RabbitMqPracApplicationTests extends MySQLTestContainer {

    @Test
    void contextLoads() {
    }

}
