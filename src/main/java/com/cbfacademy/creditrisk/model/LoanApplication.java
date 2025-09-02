package com.cbfacademy.creditrisk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * LoanApplication stores details of loan requests.
 * Includes risk assessment and decision fields.
 */
@Entity
@Table(name = "loan_applications")
public class LoanApplication extends BaseEntity {

    private Long borrowerId; // Reference to borrower
    private Double loanAmount;
    private String loanType;
    private Integer termMonths;
    private Double riskScore; // Calculated risk score
    private String riskGrade; // Low, Medium, High
    private String decision;  // Approve or Reject

    // Getters and setters omitted
}
