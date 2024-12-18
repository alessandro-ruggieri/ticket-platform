package org.lessons.milestone.repository;

import org.lessons.milestone.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByTitleContaining(String title); 
    List<Ticket> findByStatus(Ticket.Status status);
    List<Ticket> findByOperatorId(Integer operatorId);
    List<Ticket> findByCategoryId(Integer categoryId);
}
