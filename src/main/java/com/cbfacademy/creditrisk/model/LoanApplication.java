package com.cbfacademy.creditrisk.model;

import jakarta.persistence.*;

/**
 * LoanApplication stores details of loan requests.
 * Includes risk assessment and decision fields.
 */
@Entity
@Table(name = "loan_applications")
public class LoanApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) // Many loans can belong to one borrower
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower; // Store the full Borrower object

    private Double loanAmount;
    private String loanType;
    private Integer termMonths;
    private Double riskScore; // Calculated risk score
    private String riskGrade; // Low, Medium, High
    private String decision;  // Approve or Reject

    // Getters
    public Borrower getBorrower() {
        return borrower;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public String getLoanType() {
        return loanType;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public Double getRiskScore() {
        return riskScore;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public String getDecision() {
        return decision;
    }

    // Setters
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public void setRiskScore(Double riskScore) {
        this.riskScore = riskScore;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
