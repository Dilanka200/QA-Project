package com.example.notesapp.service;

import com.example.notesapp.model.Note;
import com.example.notesapp.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note testNote;

    @BeforeEach
    void setUp() {
        testNote = new Note();
        testNote.setId(1L);
        testNote.setTitle("Test Note");
        testNote.setContent("Test Content");
    }

    // Test 1: Create note with valid data
    @Test
    void createNote_WithValidData_ShouldReturnSavedNote() {
        // Given (Arrange)
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // When (Act)
        Note result = noteService.createNote(testNote);

        // Then (Assert)
        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
        verify(noteRepository, times(1)).save(any(Note.class));
    }
    @Test
    void createNote_TitleTooShort_ShouldThrowException() {
        Note n = new Note();
        n.setTitle("Hi"); // 2 chars
        n.setContent("short");
        assertThrows(IllegalArgumentException.class, () -> noteService.createNote(n));
    }
    // Test 2: Create note with empty title should throw exception
    @Test
    void createNote_WithEmptyTitle_ShouldThrowException() {
        // Given
        Note invalidNote = new Note();
        invalidNote.setTitle("");
        invalidNote.setContent("Content");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            noteService.createNote(invalidNote);
        });

        verify(noteRepository, never()).save(any(Note.class));
    }

    // Test 3: Create note with title exceeding 100 characters
    @Test
    void createNote_WithLongTitle_ShouldThrowException() {
        // Given
        Note invalidNote = new Note();
        invalidNote.setTitle("a".repeat(101)); // 101 characters
        invalidNote.setContent("Content");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            noteService.createNote(invalidNote);
        });
    }

    // Test 4: Update existing note
    @Test
    void updateNote_WithValidData_ShouldReturnUpdatedNote() {
        // Given
        Note updatedDetails = new Note();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setContent("Updated Content");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // When
        Note result = noteService.updateNote(1L, updatedDetails);

        // Then
        assertNotNull(result);
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    // Test 5: Delete existing note
    @Test
    void deleteNote_WithExistingId_ShouldDeleteSuccessfully() {
        // Given
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));
        doNothing().when(noteRepository).delete(testNote);

        // When
        noteService.deleteNote(1L);

        // Then
        verify(noteRepository, times(1)).delete(testNote);
    }
}
