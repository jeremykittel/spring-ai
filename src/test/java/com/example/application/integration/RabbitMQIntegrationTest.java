package com.example.application.integration;

import com.example.application.consumer.RabbitMQConsumer;
import com.example.application.producer.RabbitMQProducer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class RabbitMQIntegrationTest {

    public static final String MESSAGE_1 = "Test message 1";
    public static final String MESSAGE_2 = "Test message 2";
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management-alpine");

    @Autowired
    RabbitMQConsumer rabbitMQConsumer;

    @Autowired
    RabbitMQProducer rabbitMQProducer;

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
        rabbitMQProducer.sendMessage(MESSAGE_1);
        rabbitMQProducer.sendMessage(MESSAGE_2);

        Flux<String> messages = rabbitMQConsumer.consumeMessages();

        StepVerifier.create(messages)
                .expectNext(MESSAGE_1)
                .expectNext(MESSAGE_2)
                .thenCancel()
                .verify();
    }
}
