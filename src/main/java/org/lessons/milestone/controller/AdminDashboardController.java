package org.lessons.milestone.controller;

import org.lessons.milestone.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminDashboardController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("tickets", ticketService.searchTicketsByTitle(search));
        } else {
            model.addAttribute("tickets", ticketService.getAllTickets());
        }
        return "admin/dashboard"; 
    }
}
