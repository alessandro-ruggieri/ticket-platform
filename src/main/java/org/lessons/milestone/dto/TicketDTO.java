package org.lessons.milestone.dto;

import java.time.LocalDateTime;

public class TicketDTO {

    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String assignedOperator;
    private LocalDateTime createdAt;
    private String status;

    public TicketDTO(Long id, String title, String content, String categoryName, String assignedOperator, LocalDateTime createdAt, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.assignedOperator = assignedOperator;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAssignedOperator() {
        return assignedOperator;
    }

    public void setAssignedOperator(String assignedOperator) {
        this.assignedOperator = assignedOperator;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

