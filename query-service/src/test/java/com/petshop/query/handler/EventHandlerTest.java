package com.petshop.query.handler;

import com.petshop.query.domain.document.AnimalDocument;
import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.repository.AnimalDocumentRepository;
import com.petshop.query.repository.AppointmentViewRepository;
import com.petshop.query.repository.ClientDocumentRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EventHandlerTest {

    @InjectMock
    AnimalDocumentRepository animalRepository;

    @InjectMock
    ClientDocumentRepository clientRepository;

    @InjectMock
    AppointmentViewRepository appointmentRepository;

    @Inject
    AnimalEventHandler animalEventHandler;

    @Inject
    ClientEventHandler clientEventHandler;

    @Inject
    AppointmentEventHandler appointmentEventHandler;

    @Test
    public void testOnAnimalCreated() {
        JsonObject json = new JsonObject();
        json.put("animalId", 1L);
        json.put("name", "Rex");
        json.put("clientId", 2L);

        animalEventHandler.onAnimalCreated(json);

        verify(animalRepository).persist(any(AnimalDocument.class));
    }

    @Test
    public void testOnClientCreated() {
        JsonObject json = new JsonObject();
        json.put("clientId", 2L);
        json.put("name", "John");
        json.put("cpf", "123");

        clientEventHandler.onClientCreated(json);

        verify(clientRepository).persist(any(ClientDocument.class));
    }

    @Test
    public void testOnAppointmentScheduled() {
        JsonObject json = new JsonObject();
        json.put("appointmentId", 10L);
        json.put("animalId", 1L);
        json.put("type", "TOSA");
        json.put("startTime", "2026-05-10T14:00:00");
        json.put("endTime", "2026-05-10T15:00:00");

        when(animalRepository.findByOriginalId(1L)).thenReturn(new AnimalDocument());

        appointmentEventHandler.onAppointmentScheduled(json);

        verify(appointmentRepository).persist(any(com.petshop.query.domain.document.AppointmentViewDocument.class));
    }

    @Test
    public void testOnAppointmentCancelled() {
        JsonObject json = new JsonObject();
        json.put("appointmentId", 10L);

        when(appointmentRepository.delete("appointmentId", 10L)).thenReturn(1L);

        appointmentEventHandler.onAppointmentCancelled(json);

        verify(appointmentRepository).delete("appointmentId", 10L);
    }
}
