package com.example.notesapp.service;

import com.example.notesapp.model.Note;
import com.example.notesapp.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
   private static final int MIN_TITLE = 3;
   private static final int MAX_TITLE = 100;

    private void validateNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null");
        }
        String title = (note.getTitle() == null ? "" : note.getTitle().trim());
        if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty");
        if (title.length() < MIN_TITLE) throw new IllegalArgumentException("Title too short");
        if (title.length() > MAX_TITLE) throw new IllegalArgumentException("Title too long");
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        validateNote(note);
        note.setTitle(sanitizeInput(note.getTitle()));
        note.setContent(sanitizeInput(note.getContent()));
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note noteDetails) {
        validateNote(noteDetails);

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        note.setTitle(sanitizeInput(noteDetails.getTitle()));
        note.setContent(sanitizeInput(noteDetails.getContent()));

        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        noteRepository.delete(note);
    }

    // Escape HTML characters so scripts render as text (do NOT strip tags)
    private String sanitizeInput(String input) {
        if (input == null) return null;
        String s = input;
        // escape & first to avoid double-encoding
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        // If you also want to escape quotes, uncomment:
        // s = s.replace("\"", "&quot;").replace("'", "&#x27;");
        return s;
    }
}