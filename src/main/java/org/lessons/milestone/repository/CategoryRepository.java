package org.lessons.milestone.repository;

import org.lessons.milestone.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Short> {
    Optional<Category> findByName(String name);
}
