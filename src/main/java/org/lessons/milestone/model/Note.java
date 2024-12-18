package org.lessons.milestone.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    @NotNull(message = "Ticket is required")
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "Author is required")
    private User author;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Content is required")
    private String content;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    
 // Getters e Setters

	public Long getId() {
		return id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

      
}
