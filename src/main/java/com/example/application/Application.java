package com.example.application;

import com.example.application.producer.RabbitMQProducer;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Sandbox", shortName = "Sandbox")
@SpringBootApplication
@Theme(value = "sandbox-theme")
public class Application implements AppShellConfigurator, CommandLineRunner {

    private final RabbitMQProducer producer;

    public Application(RabbitMQProducer producer) {
        this.producer = producer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        producer.sendMessage("Test");
    }
}
