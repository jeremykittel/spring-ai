package com.example.application.endpoints.reactive;

import com.example.application.consumer.RabbitMQConsumer;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
@Slf4j
public class ReactiveEndpoint {

    private final RabbitMQConsumer consumer;

    public ReactiveEndpoint(RabbitMQConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Retrieves a Flux of messages.
     * <p>
     * This method consumes messages from a RabbitMQ queue and returns a Flux
     * that represents the stream of messages. The Flux is configured to handle
     * backpressure by dropping any excess elements and logs each emitted element.
     * </p>
     *
     * @return a Flux of messages
     */
    public Flux<@Nonnull String> getMessages() {
        return this.consumer.consumeMessages().onBackpressureDrop().log();
    }

    /**
     * Retrieves an EndpointSubscription for cancellable AMQP messages.
     * <p>
     * This method returns an EndpointSubscription that represents the stream
     * of AMQP messages from a RabbitMQ queue. The EndpointSubscription allows
     * the subscriber to cancel the subscription when needed.
     * </p>
     *
     * @return an EndpointSubscription for cancellable AMQP messages
     */
    public EndpointSubscription<@Nonnull String> getAmqpMessageCancellable() {
        return EndpointSubscription.of(getMessages(), () -> log.info("Subscription has been cancelled"));
    }
}
