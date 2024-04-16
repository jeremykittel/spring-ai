package com.example.application.integration;

import com.example.application.consumer.RabbitMQConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class RabbitMQIT {

    public static final String ROUTING_KEY = "sandbox";
    public static final String MESSAGE = "Test";
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management-alpine");

    @Autowired
    private RabbitTemplate template;

    @Autowired
    RabbitMQConsumer rabbitMQConsumer;

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @BeforeAll
    static void before() {
        rabbitMQContainer.start();
    }

    @AfterAll
    static void after() {
        rabbitMQContainer.stop();
    }

    @Test
    public void givenValidParams_whenServiceIsCalled_theListenerShouldConsumeTheMessage() {
        template.convertAndSend(ROUTING_KEY, MESSAGE);
        rabbitMQConsumer.consumeMessages()
                .as(StepVerifier::create)
                .expectNext(MESSAGE)
                .expectNextCount(1)
                .verifyComplete();
    }
}
