package com.petshop.query.handler;

import com.petshop.query.domain.document.AnimalDocument;
import com.petshop.query.domain.document.AppointmentViewDocument;
import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.repository.AnimalDocumentRepository;
import com.petshop.query.repository.AppointmentViewRepository;
import com.petshop.query.repository.ClientDocumentRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class EventHandlerTests {

    @Inject
    ClientEventHandler clientEventHandler;

    @Inject
    AnimalEventHandler animalEventHandler;

    @Inject
    AppointmentEventHandler appointmentEventHandler;

    @InjectMock
    ClientDocumentRepository clientRepository;

    @InjectMock
    AnimalDocumentRepository animalRepository;

    @InjectMock
    AppointmentViewRepository appointmentRepository;

    @Test
    public void testOnClientCreated() {
        JsonObject json = new JsonObject()
                .put("clientId", 1)
                .put("name", "John")
                .put("cpf", "123");

        clientEventHandler.onClientCreated(json);

        verify(clientRepository).persist(any(ClientDocument.class));
    }

    @Test
    public void testOnAnimalCreated() {
        JsonObject json = new JsonObject()
                .put("animalId", 1)
                .put("name", "Rex")
                .put("clientId", 10);

        animalEventHandler.onAnimalCreated(json);

        verify(animalRepository).persist(any(AnimalDocument.class));
    }

    @Test
    public void testOnAppointmentScheduled() {
        JsonObject json = new JsonObject()
                .put("appointmentId", 1)
                .put("animalId", 10)
                .put("type", "BANHO")
                .put("startTime", LocalDateTime.now().toString())
                .put("endTime", LocalDateTime.now().plusHours(1).toString());

        AnimalDocument animal = new AnimalDocument();
        animal.name = "Rex";
        animal.clientId = 20L;
        
        ClientDocument client = new ClientDocument();
        client.name = "John";

        when(animalRepository.findByOriginalId(10L)).thenReturn(animal);
        when(clientRepository.findByOriginalId(20L)).thenReturn(client);

        appointmentEventHandler.onAppointmentScheduled(json);

        verify(appointmentRepository).persist(any(AppointmentViewDocument.class));
    }

    @Test
    public void testOnAppointmentCancelled() {
        JsonObject json = new JsonObject()
                .put("appointmentId", 1);

        appointmentEventHandler.onAppointmentCancelled(json);

        verify(appointmentRepository).delete(anyString(), (Object) any());
    }
}
