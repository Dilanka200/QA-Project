package com.example.notesapp.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteApiTest {

    @LocalServerPort
    private int port;

    private static Long createdNoteId;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    // Test 1: Create a note
    @Test
    @Order(1)
    public void testCreateNote() {
        createdNoteId = given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"API Test Note\",\"content\":\"Testing with REST Assured\"}")
                .when()
                .post("/api/notes")
                .then()
                .statusCode(201)
                .body("title", equalTo("API Test Note"))
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    // Test 2: Get all notes
    @Test
    @Order(2)
    public void testGetAllNotes() {
        when()
                .get("/api/notes")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)));
    }

    // Test 3: Get specific note
    @Test
    @Order(3)
    public void testGetNoteById() {
        when()
                .get("/api/notes/" + createdNoteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdNoteId.intValue()))
                .body("title", equalTo("API Test Note"));
    }

    // Test 4: Update note
    @Test
    @Order(4)
    public void testUpdateNote() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Updated API Test Note\",\"content\":\"Updated content\"}")
                .when()
                .put("/api/notes/" + createdNoteId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated API Test Note"));
    }

    // Test 5: Delete note
    @Test
    @Order(5)
    public void testDeleteNote() {
        when()
                .delete("/api/notes/" + createdNoteId)
                .then()
                .statusCode(204);

        // Verify it's deleted
        when()
                .get("/api/notes/" + createdNoteId)
                .then()
                .statusCode(404);
    }

    // Test 6: Create note with empty title
    @Test
    @Order(6)
    public void testCreateNoteWithEmptyTitle() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"\",\"content\":\"Content without title\"}")
                .when()
                .post("/api/notes")
                .then()
                .statusCode(400);
    }
}
