package org.lessons.milestone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be at most 100 characters long")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Ticket> tickets;

 // Getters e Setters
    
	public Short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    
}
