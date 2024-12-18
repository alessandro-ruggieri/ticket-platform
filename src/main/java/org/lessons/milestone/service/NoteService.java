package org.lessons.milestone.service;

import org.lessons.milestone.model.Note;
import org.lessons.milestone.model.Ticket;
import org.lessons.milestone.model.User;
import org.lessons.milestone.repository.NoteRepository;
import org.lessons.milestone.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserService userService;

    // Ottieni tutte le note
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Ottieni una nota per ID
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    // Modifica una nota
    public Note updateNote(Long id, Note noteDetails) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.setContent(noteDetails.getContent());
        return noteRepository.save(note);
    }

    // Elimina una nota
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    // Ottieni tutte le note di un ticket
    public List<Note> getNotesByTicketId(Long ticketId) {
        return noteRepository.findByTicketId(ticketId);
    }
    
 // Aggiungi una nota a un ticket
    public Note addNoteToTicket(Long ticketId, String noteText, String authorEmail) {
        // Recupera il ticket dal database
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        // Recupera l'utente per email
        User author = userService.getUserByEmail(authorEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Crea la nota e associa il ticket
        Note note = new Note();
        note.setTicket(ticket);  
        note.setContent(noteText);  
        note.setAuthor(author); 
        note.setCreatedAt(LocalDateTime.now());  
        return noteRepository.save(note); 
    }
}
