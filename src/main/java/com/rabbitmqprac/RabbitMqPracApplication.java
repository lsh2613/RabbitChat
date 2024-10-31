package com.rabbitmqprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * ws://localhost:8080/chat/inbox
 * /exchange/chat.exchange/room.1
 * /pub/chat.message
 * {"chatRoomId":"1", "memberId":"1", "message":"hello"}
 */
@SpringBootApplication
public class RabbitMqPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqPracApplication.class, args);
    }

}
