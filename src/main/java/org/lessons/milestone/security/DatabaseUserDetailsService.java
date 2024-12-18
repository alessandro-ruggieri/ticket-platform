package org.lessons.milestone.security;

import org.lessons.milestone.model.User;
import org.lessons.milestone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<User> userOpt = userRepository.findByEmail(username);

        if (userOpt.isPresent()) {
        	System.out.println("Utente trovato: " + userOpt.get().getEmail());
            return new DatabaseUserDetails(userOpt.get());
        } else {
        	System.out.println("Utente non trovato: " + username);
            throw new UsernameNotFoundException("User with email " + username + " not found.");
        }
    }
}
