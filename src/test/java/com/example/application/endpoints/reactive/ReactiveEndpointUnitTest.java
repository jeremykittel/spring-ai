package com.example.application.endpoints.reactive;

import com.example.application.consumer.RabbitMQConsumer;
import dev.hilla.EndpointSubscription;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReactiveEndpointUnitTest {

    @InjectMocks
    private ReactiveEndpoint reactiveEndpoint;

    @Mock
    private RabbitMQConsumer rabbitMQConsumer;

    /**
     * Test for getAmqpMessageCancellable method of ReactiveEndpoint
     * This test validates that the ReactiveEndpoint.getAmqpMessageCancellable
     * method correctly creates an EndpointSubscription from a Flux stream of messages.
     */
    @Test
    public void testGetAmqpMessageCancellable() {
        // mock to return a Flux of predefined strings when consumeMessages is called
        when(rabbitMQConsumer.consumeMessages()).thenReturn(Flux.just("message1", "message2", "message3"));

        // call the method being tested
        EndpointSubscription<String> result = reactiveEndpoint.getAmqpMessageCancellable();

        // verify that the Flux in the EndpointSubscription emits the correct messages
        StepVerifier.create(result.getFlux())
                .expectNext("message1")
                .expectNext("message2")
                .expectNext("message3")
                .verifyComplete();

        //TODO: it's complicated to test the cancellation of the flux, 
        //as it involves handling of system resources, such as network connections. 
        //Usually, such things are tested in integration tests rather than unit tests.
    }

    /**
     * Test class for ReactiveEndpoint.java
     * Methods tested: getMessages()
     * <p>
     * This class tests the functionality of the getMessages() method in the ReactiveEndpoint class.
     * The getMessages() method retrieves messages as Flux from a RabbitMQ queue.
     */
    @Test
    public void testGetMessages() {
        // Arrange
        List<String> data = Arrays.asList("message 1", "message 2", "message 3");
        RabbitMQConsumer mockedConsumer = Mockito.mock(RabbitMQConsumer.class);
        Mockito.when(mockedConsumer.consumeMessages()).thenReturn(Flux.fromIterable(data));
        ReactiveEndpoint reactiveEndpoint = new ReactiveEndpoint(mockedConsumer);

        // Act
        Flux<String> result = reactiveEndpoint.getMessages();

        // Assert
        StepVerifier.create(result)
                .expectNext("message 1")
                .expectNext("message 2")
                .expectNext("message 3")
                .verifyComplete();
    }
}