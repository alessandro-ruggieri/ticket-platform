package org.lessons.milestone.service;

import org.lessons.milestone.model.Role;
import org.lessons.milestone.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Ottieni tutti i ruoli
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Ottieni un ruolo per ID
    public Optional<Role> getRoleById(Short id) {
        return roleRepository.findById(id);
    }
}
