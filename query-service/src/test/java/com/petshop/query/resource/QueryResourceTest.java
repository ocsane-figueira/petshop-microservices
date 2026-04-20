package com.petshop.query.resource;

import com.petshop.query.domain.document.AppointmentViewDocument;
import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.repository.AppointmentViewRepository;
import com.petshop.query.repository.ClientDocumentRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
public class QueryResourceTest {

    @InjectMock
    AppointmentViewRepository appointmentRepository;

    @InjectMock
    ClientDocumentRepository clientRepository;

    @Test
    public void testGetAppointments() {
        AppointmentViewDocument doc = new AppointmentViewDocument();
        doc.appointmentId = 1L;
        doc.clientName = "Test Client";
        doc.animalName = "Test Animal";

        when(appointmentRepository.listAll()).thenReturn(Collections.singletonList(doc));

        given()
          .when().get("/api/query/appointments")
          .then()
             .statusCode(200)
             .body("$.size()", is(1))
             .body("[0].clientName", is("Test Client"));
    }

    @Test
    public void testGetClients() {
        ClientDocument doc = new ClientDocument();
        doc.id = 1L;
        doc.name = "Test Client";

        when(clientRepository.listAll()).thenReturn(Collections.singletonList(doc));

        given()
          .when().get("/api/query/clients")
          .then()
             .statusCode(200)
             .body("$.size()", is(1))
             .body("[0].name", is("Test Client"));
    }
}
