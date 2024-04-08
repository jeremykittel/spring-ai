package com.example.application.endpoints.reactive;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
@BrowserCallable
@AnonymousAllowed
@Slf4j
public class ReactiveEndpoint {

    public ReactiveEndpoint() {
    }

    public Flux<@Nonnull String> getClock() {
        return Flux.interval(Duration.ofSeconds(1))
                .onBackpressureDrop()
                .map(_interval -> new Date().toString());
    }

    public EndpointSubscription<@Nonnull String> getClockCancellable() {
        return EndpointSubscription.of(getClock(), () -> log.info("Subscription has been cancelled"));
    }
}
