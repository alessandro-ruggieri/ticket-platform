package org.lessons.milestone.service;

import org.lessons.milestone.model.Ticket;
import org.lessons.milestone.model.User;
import org.lessons.milestone.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Ottieni tutti i ticket
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Ottieni un ticket per ID
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    // Crea un nuovo ticket
    public Ticket createTicket(Ticket ticket) {
    	// Verifica se l'operatore è ACTIVE prima di assegnarlo
        if (ticket.getOperator() != null && ticket.getOperator().getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("L'operatore assegnato deve essere in stato ACTIVE");
        }
        return ticketRepository.save(ticket);
    }

    // Modifica un ticket
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
    	Ticket ticket = ticketRepository.findById(id)
    		    .orElseThrow(() -> new RuntimeException("Ticket con ID " + id + " non trovato"));
        ticket.setTitle(ticketDetails.getTitle());
        ticket.setDescription(ticketDetails.getDescription());
        ticket.setStatus(ticketDetails.getStatus());
        ticket.setCategory(ticketDetails.getCategory());
        // Verifica se l'operatore è ACTIVE prima di assegnarlo
        if (ticketDetails.getOperator() != null && ticketDetails.getOperator().getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("L'operatore assegnato deve essere in stato ACTIVE");
        }
        ticket.setOperator(ticketDetails.getOperator());
        System.out.println("Ticket aggiornato: " + ticket);
        return ticketRepository.save(ticket);
    }
    
    // Salva o aggiorna un ticket
    public Ticket saveTicket(Ticket ticket) {
    	System.out.println("Ticket aggiornato: " + ticket);
        return ticketRepository.save(ticket);
    }


    // Elimina un ticket
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
    
    // Ottieni ticket per ID della categoria
    public List<Ticket> getTicketsByCategoryId(Integer categoryId) {
        return ticketRepository.findByCategoryId(categoryId);  
    }

    // Ottieni i ticket per stato
    public List<Ticket> getTicketsByStatus(Ticket.Status status) {
        return ticketRepository.findByStatus(status);
    }
    
    //Ottieni i ticket per ricerca nel titolo
    public List<Ticket> searchTicketsByTitle(String title) {
        return ticketRepository.findByTitleContaining(title);
    }
    
  //Ottieni i ticket per ricerca da ID Operatore
    public List<Ticket> getTicketsByOperatorId(Integer operatorId) {
        return ticketRepository.findByOperatorId(operatorId);  
    }
}

