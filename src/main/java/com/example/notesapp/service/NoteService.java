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

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        validateNote(note);
        // Make sure these lines are present:
        note.setTitle(sanitizeInput(note.getTitle()));
        note.setContent(sanitizeInput(note.getContent()));
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note noteDetails) {
        validateNote(noteDetails);

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        // Make sure these lines use sanitizeInput:
        note.setTitle(sanitizeInput(noteDetails.getTitle()));
        note.setContent(sanitizeInput(noteDetails.getContent()));

        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        noteRepository.delete(note);
    }

    private void validateNote(Note note) {
        if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Note title cannot be empty");
        }
        if (note.getTitle().length() > 100) {
            throw new IllegalArgumentException("Note title cannot exceed 100 characters");
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("<script>", "")
                .replaceAll("</script>", "")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
