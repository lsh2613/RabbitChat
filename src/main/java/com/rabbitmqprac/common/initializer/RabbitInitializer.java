package com.rabbitmqprac.common.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitInitializer {
    private final AmqpAdmin amqpAdmin;
    private final Queue queue;
    private final TopicExchange exchange;
    private final Binding binding;

    @PostConstruct
    public void initRabbitMQ() {
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}