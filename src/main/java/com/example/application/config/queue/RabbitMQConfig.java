package com.example.application.config.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Creates a Spring Bean for a RabbitMQ queue.
     *
     * @return The created Queue object.
     */
    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    /**
     * Creates a Spring bean for a RabbitMQ exchange.
     *
     * @return The created TopicExchange object.
     */
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    /**
     * Create a binding between a queue and an exchange using a routing key.
     * The binding will be created as a Spring Bean.
     *
     * @return The created Binding object.
     */
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }
}
