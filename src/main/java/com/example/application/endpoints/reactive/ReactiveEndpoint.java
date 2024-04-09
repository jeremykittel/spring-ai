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
     * This method internally calls the consumeMessages() method of the RabbitMQConsumer class to retrieve the messages.
     * It returns a Flux of messages.
     *
     * @return a Flux of messages
     */
    public Flux<String> getMessages() {
        return this.consumer.consumeMessages();
    }

    /**
     * Retrieves a Flux of non-null Strings representing the messages.
     * <p>
     * This method internally calls the {@link #getMessages()} method to retrieve the messages and print them to the console.
     * It then returns the Flux of messages.
     *
     * @return a Flux of non-null Strings representing the messages
     */
    public Flux<@Nonnull String> getMessage() {
        log.info(getMessages().toString());
        return getMessages();
    }

    /**
     * Returns an EndpointSubscription object that represents a cancellable subscription to the AMQP message.
     *
     * @return an EndpointSubscription object representing the AMQP message subscription
     */
    public EndpointSubscription<@Nonnull String> getAmqpMessageCancellable() {
        return EndpointSubscription.of(getMessage(), () -> log.info("Subscription has been cancelled"));
    }
}
