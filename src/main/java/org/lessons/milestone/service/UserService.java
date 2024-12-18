package org.lessons.milestone.service;

import org.lessons.milestone.model.User;
import org.lessons.milestone.model.User.Status;
import org.lessons.milestone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Ottieni tutti gli utenti
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Ottieni un utente per ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Ottieni un utente per email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

 // Ottieni tutti gli utenti con il ruolo di operatore
    public List<User> findAllOperators() {
        return userRepository.findByRoleName("operator");
    }
    
 // Ottieni tutti gli operatori con lo status Active
    public List<User> findActiveOperators() {
        return userRepository.findByRoleNameAndStatus("operator", Status.ACTIVE);
    }

    // Metodo per salvare un operatore
    public void saveOperator(User operator) {
        userRepository.save(operator);
    }

    
}
