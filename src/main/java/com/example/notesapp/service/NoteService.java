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



    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.replaceAll("<script>", "")
                .replaceAll("</script>", "")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
