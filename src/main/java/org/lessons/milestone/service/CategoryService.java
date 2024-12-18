package org.lessons.milestone.service;

import org.lessons.milestone.model.Category;
import org.lessons.milestone.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Ottieni tutte le categorie
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Ottieni una categoria per ID
    public Optional<Category> getCategoryById(Short id) {
        return categoryRepository.findById(id);
    }

}
