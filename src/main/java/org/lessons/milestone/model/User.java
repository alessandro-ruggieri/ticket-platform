package org.lessons.milestone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "Surname is required")
    private String surname;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    @NotNull(message = "Status is required")
    private Status status = Status.ACTIVE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @NotNull(message = "Role is required")
    private Role role;

    @OneToMany(mappedBy = "operator")
    private Set<Ticket> assignedTickets;

    @OneToMany(mappedBy = "author")
    private Set<Note> notes;

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getFullName() {
        return name + " " + surname;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(Set<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }
}

