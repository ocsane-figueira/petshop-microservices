package com.petshop.resource;

import com.petshop.application.dto.ClientDTO;
import com.petshop.application.service.ClientService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ClientResourceTest {

    @InjectMock
    ClientService clientService;

    @Test
    public void testCreateClientEndpoint() {
        ClientDTO mockDto = new ClientDTO();
        mockDto.id = 10L;
        mockDto.name = "John Doe";
        
        when(clientService.createClient(any(ClientDTO.class))).thenReturn(mockDto);

        String payload = "{\"name\":\"John Doe\", \"cpf\":\"11122233344\", \"email\":\"john@doe.com\"}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/clients")
          .then()
             .statusCode(201);
    }
}
