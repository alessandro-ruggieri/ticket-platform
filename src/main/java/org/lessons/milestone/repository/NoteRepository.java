package org.lessons.milestone.repository;

import org.lessons.milestone.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTicketId(Long ticketId);
}
