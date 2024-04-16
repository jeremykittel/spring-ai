package com.example.application.integration;

import com.example.application.consumer.RabbitMQConsumer;
import com.example.application.producer.RabbitMQProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class RabbitMQIntegrationTest extends BaseIntegrationTestConfig {

    public static final String MESSAGE_1 = "Test message 1";
    public static final String MESSAGE_2 = "Test message 2";


    @Autowired
    RabbitMQConsumer rabbitMQConsumer;

    @Autowired
    RabbitMQProducer rabbitMQProducer;

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
