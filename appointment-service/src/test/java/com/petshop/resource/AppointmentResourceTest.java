package com.petshop.resource;

import com.petshop.application.dto.AppointmentDTO;
import com.petshop.application.service.AppointmentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.petshop.domain.entity.AppointmentType;

@QuarkusTest
public class AppointmentResourceTest {

    @InjectMock
    AppointmentService appointmentService;

    @Test
    public void testScheduleEndpoint() {
        AppointmentDTO mockDto = new AppointmentDTO();
        mockDto.setId(20L);
        mockDto.setType(AppointmentType.TOSA);
        
        when(appointmentService.schedule(any(AppointmentDTO.class))).thenReturn(mockDto);

        String payload = "{\"animalId\": 1, \"type\": \"TOSA\", \"startTime\": \"2026-05-10T14:00:00\", \"endTime\": \"2026-05-10T15:00:00\"}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/appointments")
          .then()
             .statusCode(201);
    }
}
