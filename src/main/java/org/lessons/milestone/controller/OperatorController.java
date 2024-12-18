package org.lessons.milestone.controller;

import org.lessons.milestone.model.User;
import org.lessons.milestone.model.Ticket;
import org.lessons.milestone.service.UserService;
import org.lessons.milestone.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OperatorController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    // Visualizza i dettagli dell'operatore
    @GetMapping("/operator/details")
    public String showOperatorDetails(Model model, Principal principal) {
        // Recupera l'operatore loggato usando l'email
        Optional<User> optionalOperator = userService.getUserByEmail(principal.getName());
        if (optionalOperator.isPresent()) {
            User operator = optionalOperator.get();
            // Recupera i ticket assegnati all'operatore usando l'ID
            List<Ticket> tickets = ticketService.getTicketsByOperatorId(operator.getId());
            
            // Verifica se tutti i ticket sono in stato COMPLETED
            boolean allCompleted = tickets.stream()
                                          .allMatch(ticket -> ticket.getStatus() == Ticket.Status.COMPLETED);

            model.addAttribute("operator", operator);
            model.addAttribute("tickets", tickets);
            model.addAttribute("statusOptions", Ticket.Status.values());
            model.addAttribute("allCompleted", allCompleted);  
        }
        return "operator/details"; 
    }

    // Cambia lo stato di un ticket
    @PostMapping("/operator/ticket/{ticketId}/status")
    public String changeTicketStatus(@PathVariable Long ticketId,
                                     @RequestParam("status") Ticket.Status status,
                                     Principal principal) {
        // Recupera l'operatore loggato usando l'email
        Optional<User> optionalOperator = userService.getUserByEmail(principal.getName());
        if (optionalOperator.isPresent()) {
            User operator = optionalOperator.get();
            Optional<Ticket> optionalTicket = ticketService.getTicketById(ticketId);

            if (optionalTicket.isPresent()) {
                Ticket ticket = optionalTicket.get();
                // Verifica che il ticket appartenga all'operatore loggato
                if (ticket.getOperator().getId().equals(operator.getId())) {
                    ticket.setStatus(status);
                    ticketService.saveTicket(ticket);
                }
            }
        }
        return "redirect:/operator/details"; 
    }

    // Imposta lo stato a ACTIVE
    @PostMapping("/operator/set-active")
    public String setOperatorActive(Principal principal) {
        Optional<User> optionalOperator = userService.getUserByEmail(principal.getName());
        if (optionalOperator.isPresent()) {
            User operator = optionalOperator.get();
            operator.setStatus(User.Status.ACTIVE);
            userService.saveOperator(operator);  
        }
        return "redirect:/operator/details";
    }

    // Imposta lo stato a INACTIVE
    @PostMapping("/operator/set-inactive")
    public String setOperatorInactive(Principal principal) {
        Optional<User> optionalOperator = userService.getUserByEmail(principal.getName());
        if (optionalOperator.isPresent()) {
            User operator = optionalOperator.get();
            operator.setStatus(User.Status.INACTIVE);
            userService.saveOperator(operator);  
        }
        return "redirect:/operator/details"; 
    }
}
