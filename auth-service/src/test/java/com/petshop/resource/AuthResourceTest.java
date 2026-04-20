package com.petshop.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testLoginSuccessAdmin() {
        String payload = "{\"username\": \"admin\", \"password\": \"admin123\"}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/auth/login")
          .then()
             .statusCode(200)
             .body("token", notNullValue())
             .body("expiresIn", is(3600));
    }

    @Test
    public void testLoginSuccessClient() {
        String payload = "{\"username\": \"cliente\", \"password\": \"cliente123\"}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/auth/login")
          .then()
             .statusCode(200)
             .body("token", notNullValue())
             .body("expiresIn", is(3600));
    }

    @Test
    public void testLoginFailure() {
        String payload = "{\"username\": \"admin\", \"password\": \"wrongpassword\"}";

        given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when().post("/api/auth/login")
          .then()
             .statusCode(401);
    }
}
