package com.example.notesapp.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    private long createNoteAndReturnId(String title, String content) {
        // Use jsonPath().getLong("id") so we don't care if backend returns Integer or Long
        return given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"" + title + "\",\"content\":\"" + content + "\"}")
                .when()
                .post("/api/notes")
                .then()
                .statusCode(201)
                .body("title", equalTo(title))
                .body("id", notNullValue())
                .extract()
                .jsonPath()
                .getLong("id");
    }

    @Test
    @Order(1)
    void testCreateNote() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"API Test Note\",\"content\":\"Testing with REST Assured\"}")
                .when()
                .post("/api/notes")
                .then()
                .statusCode(201)
                .body("title", equalTo("API Test Note"))
                .body("id", notNullValue());
    }

    @Test
    @Order(2)
    void testGetAllNotes() {
        // Ensure at least one exists
        createNoteAndReturnId("List Note", "For listing");

        when()
                .get("/api/notes")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)));
    }

    @Test
    @Order(3)
    void testGetNoteById() {
        long id = createNoteAndReturnId("Specific Note", "Specific Content");

        when()
                .get("/api/notes/{id}", id)   // use path param, not string concat
                .then()
                .statusCode(200)
                // JSON numbers may be int-sized; compare against int value
                .body("id", equalTo((int) id))
                .body("title", equalTo("Specific Note"));
    }

    @Test
    @Order(4)
    void testUpdateNote() {
        long id = createNoteAndReturnId("Original Title", "Original Content");

        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Updated API Test Note\",\"content\":\"Updated content\"}")
                .when()
                .put("/api/notes/{id}", id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated API Test Note"))
                .body("content", equalTo("Updated content"));
    }

    @Test
    @Order(5)
    void testDeleteNote() {
        long id = createNoteAndReturnId("To Delete", "Will be deleted");

        when()
                .delete("/api/notes/{id}", id)
                .then()
                .statusCode(204);

        when()
                .get("/api/notes/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    void testCreateNoteWithEmptyTitle() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"\",\"content\":\"Content without title\"}")
                .when()
                .post("/api/notes")
                .then()
                .statusCode(400);
    }
}