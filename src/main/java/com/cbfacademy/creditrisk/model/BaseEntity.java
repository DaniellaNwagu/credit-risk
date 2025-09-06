package com.cbfacademy.creditrisk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 * BaseEntity provides common fields for all entities.
 * Demonstrates inheritance and adds automatic auditing.
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    private LocalDateTime createdAt; // Timestamp for entity creation
    private LocalDateTime updatedAt; // Timestamp for last update

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set creation time automatically
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Update timestamp automatically
    }

    public Long getId() { return id; }
}
