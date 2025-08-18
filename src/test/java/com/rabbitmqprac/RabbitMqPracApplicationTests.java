package com.rabbitmqprac;

import com.rabbitmqprac.common.container.MySQLTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitMqPracApplicationTests extends MySQLTestContainer {

    @Test
    void contextLoads() {
    }

}
