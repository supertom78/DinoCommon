package fr.liksi.api;


import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.http.ContentType.HTML;
import static io.restassured.http.ContentType.JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestingSwaggerAvailable {

    @Test
    void shouldExposeSwaggerUI() {
        RestAssured.when().get("/swagger-ui/index.html")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(HTML);
    }

    @Test
    void shouldExposeOpenApiSpec() {
        RestAssured.when().get("/v3/api-docs")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(JSON);
    }
}
