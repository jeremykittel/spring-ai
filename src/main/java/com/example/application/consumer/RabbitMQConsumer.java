package com.example.application.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class RabbitMQConsumer {

    private final Sinks.Many<String> sink;

    /**
     * RabbitMQConsumer is a class that consumes messages from a RabbitMQ queue and provides a Flux of messages.
     */
    public RabbitMQConsumer() {
        this.sink = Sinks.many().multicast().directBestEffort();
    }

    /**
     * Method to consume a message from a RabbitMQ queue.
     *
     * @param message the message received from the RabbitMQ queue
     */
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message){
        log.info(String.format("Received message -> %s", message));
        sink.tryEmitNext(message);
    }

    /**
     * Retrieves a Flux of messages by consuming from a RabbitMQ queue.
     *
     * @return a Flux of messages
     */
    public Flux<String> consumeMessages() {
        return this.sink.asFlux();
    }
}
