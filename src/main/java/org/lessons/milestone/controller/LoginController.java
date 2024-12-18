package org.lessons.milestone.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/")
	public String redirectAfterLogin(Authentication authentication) {
	    // Verifica se l'utente è già autenticato
	    if (authentication != null && authentication.isAuthenticated()) {
	        // Se autenticato, controlla il ruolo dell'utente
	        if (authentication.getAuthorities().stream()
	                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
	            return "redirect:/admin/dashboard"; // Se è admin, redirigi alla dashboard admin
	        } else if (authentication.getAuthorities().stream()
	                .anyMatch(role -> role.getAuthority().equals("ROLE_OPERATOR"))) {
	            return "redirect:/operator/details"; // Se è operatore, redirigi alla dashboard operatore
	        }
	    }
	    // Se non autenticato o se non ha un ruolo valido, reindirizza al login
	    return "redirect:/login";
	}

}
