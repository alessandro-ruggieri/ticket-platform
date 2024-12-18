package org.lessons.milestone.controller;

import org.lessons.milestone.service.TicketService;
import org.lessons.milestone.model.Category;
import org.lessons.milestone.model.Ticket;
import org.lessons.milestone.repository.CategoryRepository;
import org.lessons.milestone.security.DatabaseUserDetails;
import org.lessons.milestone.service.NoteService;
import org.lessons.milestone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    // Visualizza i dettagli del ticket
    @GetMapping("/{id}")
    public String showTicketDetails(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("ticket", ticketService.getTicketById(id).orElseThrow(() -> new RuntimeException("Ticket not found")));
        model.addAttribute("notes", noteService.getNotesByTicketId(id));
        model.addAttribute("statusOptions", Ticket.Status.values());

        // Verifica se l'utente è un admin o un operatore per aggiungere la possibilità di cambiare lo stato o aggiungere una nota
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATOR"))) {
            model.addAttribute("canChangeStatus", true);  
            model.addAttribute("canAddNote", true);       
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("canChangeStatus", true);  
            model.addAttribute("canAddNote", true);       
        }
        
     // Aggiunto il flag isAdmin per il template
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin); 

        return "ticket/details";
    }

    @PostMapping("/{id}/add-note")
    public String addNote(
            @PathVariable Long id,
            @RequestParam String noteText,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // Recupera il ticket
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        // Recupera l'utente autenticato
        DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
        String operatorEmail = userDetails.getUsername(); 

        // Controlla se il ticket è assegnato all'operatore o se l'utente è un admin
        if (!ticket.getOperator().getEmail().equals(operatorEmail) &&
            !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Non hai i permessi per aggiungere una nota a questo ticket.");
        }

        try {
            noteService.addNoteToTicket(id, noteText, operatorEmail);
            redirectAttributes.addFlashAttribute("successMessage", "Nota aggiunta con successo!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore nell'aggiungere la nota: " + e.getMessage());
        }

        return "redirect:/tickets/" + id; 
    }
    
    @PostMapping("/{ticketId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable Long ticketId, @PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return "redirect:/tickets/{ticketId}";
    }



    // Metodo per cambiare stato ad un ticket
    @PostMapping("/{id}/change-status")
    public String changeTicketStatus(@PathVariable Long id, @RequestParam Ticket.Status status, Authentication authentication) {
        // Controlla che solo l'admin o l'operatore assegnato al ticket possano cambiare lo stato
        Ticket ticket = ticketService.getTicketById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERATOR"))) {
            if (ticket.getOperator() == null || !ticket.getOperator().getEmail().equals(authentication.getName())) {
                throw new IllegalStateException("Non puoi cambiare lo stato di un ticket che non ti è stato assegnato.");
            }
        }

        ticket.setStatus(status);
        ticketService.updateTicket(id, ticket);
        return "redirect:/tickets/" + id;
    }

    // Metodo per la creazione di un ticket
    @GetMapping("/create")
    public String showCreateTicketForm(Model model, Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Accesso negato. Solo gli admin possono creare ticket.");
        }
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("operators", userService.findActiveOperators());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "ticket/create";
    }

    @PostMapping("/create")
    public String createTicket(@Valid @ModelAttribute Ticket ticket, BindingResult result, Model model, Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Accesso negato. Solo gli admin possono creare ticket.");
        }
        if (result.hasErrors()) {
            model.addAttribute("operators", userService.findActiveOperators());
            model.addAttribute("categories", categoryRepository.findAll());
            return "ticket/create";
        }
        ticketService.createTicket(ticket);
        return "redirect:/admin/dashboard";
    }

    // Metodo per la modifica di un ticket
    @GetMapping("/edit/{id}")
    public String showEditTicketForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Accesso negato. Solo gli admin possono modificare i ticket.");
        }
        model.addAttribute("ticket", ticketService.getTicketById(id).orElseThrow(() -> new RuntimeException("Ticket not found")));
        model.addAttribute("operators", userService.findActiveOperators());
        List<Category> categories = categoryRepository.findAll();  
        model.addAttribute("categories", categories);
        return "ticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTicket(@PathVariable Long id, @Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Accesso negato. Solo gli admin possono modificare i ticket.");
        }

        if (result.hasErrors()) {
            model.addAttribute("operators", userService.findActiveOperators());
            return "ticket/edit";
        }

        try {
            Ticket existingTicket = ticketService.getTicketById(id)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            existingTicket.setTitle(ticket.getTitle());
            existingTicket.setDescription(ticket.getDescription());
            existingTicket.setStatus(ticket.getStatus());
            existingTicket.setOperator(ticket.getOperator());

            ticketService.updateTicket(id, existingTicket);

            redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
            return "redirect:/tickets/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating ticket: " + e.getMessage());
            return "ticket/edit";
        }
    }

    // Metodo per eliminare un ticket
    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes, Authentication authentication) {
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Accesso negato. Solo gli admin possono eliminare i ticket.");
        }

        try {
            ticketService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket eliminato con successo!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del ticket.");
            return "redirect:/tickets";
        }
    }
}
