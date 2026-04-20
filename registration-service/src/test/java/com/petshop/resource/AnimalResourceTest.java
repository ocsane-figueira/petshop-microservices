package com.petshop.resource;

import com.petshop.application.dto.AnimalDTO;
import com.petshop.application.service.AnimalService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AnimalResourceTest {

    @InjectMock
    AnimalService animalService;

    @Test
    public void testCreateAnimalEndpoint() {
        AnimalDTO mockDto = new AnimalDTO();
        mockDto.id = 5L;
        mockDto.name = "Max";
        
        when(animalService.createAnimal(any(AnimalDTO.class))).thenReturn(mockDto);

        String payload = "{\"name\":\"Max\", \"species\":\"Cat\", \"clientId\":1}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/animals")
          .then()
             .statusCode(201);
    }
}
