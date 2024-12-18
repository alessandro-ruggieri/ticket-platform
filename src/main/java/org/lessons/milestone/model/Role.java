package org.lessons.milestone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;


@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must be at most 50 characters long")
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;

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
