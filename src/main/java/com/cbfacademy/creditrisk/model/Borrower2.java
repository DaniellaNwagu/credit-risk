package com.cbfacademy.creditrisk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Borrower entity stores borrower information.
 * Inherits auditing fields from BaseEntity.
 */
@Entity
@Table(name = "borrowers")
public class Borrower extends BaseEntity {

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String ssnNin; // Sensitive, consider encryption in production
    private String employmentStatus;
    private Double annualIncome;

    // Getters and setters omitted for brevity
}
