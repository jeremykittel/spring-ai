package com.example.application.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@DirtiesContext
public class BaseIntegrationTestConfig {

    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management-alpine");

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
}
