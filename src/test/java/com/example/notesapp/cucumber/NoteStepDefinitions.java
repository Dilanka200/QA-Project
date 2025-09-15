package com.example.notesapp.cucumber;

import com.example.notesapp.model.Note;
import com.example.notesapp.service.NoteService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class NoteStepDefinitions {

    @Autowired
    private NoteService noteService;

    private Note note;
    private Note savedNote;
    private Exception exception;

    @Given("I have a note with title {string} and content {string}")
    public void i_have_a_note_with_title_and_content(String title, String content) {
        note = new Note();
        note.setTitle(title);
        note.setContent(content);
    }

    @Given("I have a note with empty title")
    public void i_have_a_note_with_empty_title() {
        note = new Note();
        note.setTitle("");
        note.setContent("Some content");
    }

    @Given("I have an existing note with ID {long}")
    public void i_have_an_existing_note_with_id(Long id) {
        note = new Note();
        note.setTitle("Existing Note");
        note.setContent("Existing Content");
        savedNote = noteService.createNote(note);
    }

    @When("I create the note")
    public void i_create_the_note() {
        try {
            savedNote = noteService.createNote(note);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I try to create the note")
    public void i_try_to_create_the_note() {
        try {
            savedNote = noteService.createNote(note);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I update the note title to {string}")
    public void i_update_the_note_title_to(String newTitle) {
        try {
            note.setTitle(newTitle);
            savedNote = noteService.updateNote(savedNote.getId(), note);
        } catch (Exception e) {
            exception = e;
        }
    }

    @When("I delete the note")
    public void i_delete_the_note() {
        try {
            noteService.deleteNote(savedNote.getId());
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the note should be saved successfully")
    public void the_note_should_be_saved_successfully() {
        assertNotNull(savedNote);
        assertNull(exception);
    }

    @Then("the note should have an ID")
    public void the_note_should_have_an_id() {
        assertNotNull(savedNote.getId());
    }

    @Then("I should get an error message")
    public void i_should_get_an_error_message() {
        assertNotNull(exception);
        assertTrue(exception instanceof IllegalArgumentException);
    }

    @Then("the note should be updated successfully")
    public void the_note_should_be_updated_successfully() {
        assertNotNull(savedNote);
        assertEquals("Updated Shopping List", savedNote.getTitle());
    }

    @Then("the note should be deleted successfully")
    public void the_note_should_be_deleted_successfully() {
        assertNull(exception);
    }
}