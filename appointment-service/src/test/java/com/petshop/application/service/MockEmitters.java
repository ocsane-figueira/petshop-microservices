package com.petshop.application.service;

import com.petshop.event.AppointmentScheduledEvent;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.inject.Produces;
import io.quarkus.test.Mock;
import org.mockito.Mockito;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MockEmitters {

    private final Emitter<AppointmentScheduledEvent> scheduledMock = Mockito.mock(Emitter.class);
    private final Emitter<com.petshop.application.event.AppointmentCancelledEvent> cancelledMock = Mockito.mock(Emitter.class);

    @Produces
    @Mock
    @org.eclipse.microprofile.reactive.messaging.Channel("appointment-scheduled")
    public Emitter<AppointmentScheduledEvent> scheduledEmitter() {
        return scheduledMock;
    }

    @Produces
    @Mock
    @org.eclipse.microprofile.reactive.messaging.Channel("appointment-cancelled")
    public Emitter<com.petshop.application.event.AppointmentCancelledEvent> cancelledEmitter() {
        return cancelledMock;
    }
}
