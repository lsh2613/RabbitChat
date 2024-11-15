package com.rabbitmqprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ws://localhost:8080/chat/inbox
 * /exchange/chat.exchange/room.1
 * /pub/chat.message
 *{"message":"hello"}
 */
@EnableAsync
@SpringBootApplication
public class RabbitMqPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqPracApplication.class, args);
    }

}
