package org.lessons.milestone.repository;

import org.lessons.milestone.model.User;
import org.lessons.milestone.model.User.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    
    List<User> findByRoleName(String roleName);
    
    
    List<User> findByRoleNameAndStatus(String roleName, Status status);
}
