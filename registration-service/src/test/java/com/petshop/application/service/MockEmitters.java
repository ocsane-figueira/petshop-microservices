package com.petshop.application.service;

import com.petshop.event.AnimalCreatedEvent;
import com.petshop.event.ClientCreatedEvent;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.inject.Produces;
import io.quarkus.test.Mock;
import org.mockito.Mockito;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MockEmitters {

    private final Emitter<ClientCreatedEvent> clientMock = Mockito.mock(Emitter.class);
    private final Emitter<AnimalCreatedEvent> animalMock = Mockito.mock(Emitter.class);

    @Produces
    @Mock
    @org.eclipse.microprofile.reactive.messaging.Channel("client-created")
    public Emitter<ClientCreatedEvent> clientEmitter() {
        return clientMock;
    }

    @Produces
    @Mock
    @org.eclipse.microprofile.reactive.messaging.Channel("animal-created")
    public Emitter<AnimalCreatedEvent> animalEmitter() {
        return animalMock;
    }
}
