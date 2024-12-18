package org.lessons.milestone.controllerAPI;

import org.lessons.milestone.dto.TicketDTO;
import org.lessons.milestone.model.Ticket;
import org.lessons.milestone.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TicketApiController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/api/tickets")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets().stream()
                .map(ticket -> new TicketDTO(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getCategory().getName(),  
                        ticket.getOperator().getFullName(), 
                        ticket.getCreatedAt(),
                        ticket.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/tickets/category/{categoryId}")
    public List<TicketDTO> getTicketsByCategory(@PathVariable Integer categoryId) {
        return ticketService.getTicketsByCategoryId(categoryId).stream()
                .map(ticket -> new TicketDTO(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getCategory().getName(),
                        ticket.getOperator().getFullName(), 
                        ticket.getCreatedAt(),
                        ticket.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/tickets/status/{status}")
    public List<TicketDTO> getTicketsByStatus(@PathVariable Ticket.Status status) {
        return ticketService.getTicketsByStatus(status).stream()
                .map(ticket -> new TicketDTO(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getCategory().getName(),
                        ticket.getOperator().getFullName(),
                        ticket.getCreatedAt(),
                        ticket.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }
}
