package com.example.notesapp.controller;

import com.example.notesapp.model.Note;
import com.example.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteService noteService;

    // Constructor
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @Valid @RequestBody Note noteDetails) {
        try {
            Note updatedNote = noteService.updateNote(id, noteDetails);
            return ResponseEntity.ok(updatedNote);
        } catch (IllegalArgumentException e) {
            // This catches validation errors
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // This catches "Note not found" errors
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}