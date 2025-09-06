package com.cbfacademy.creditrisk.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoanApplication entity
 * Tests the basic functionality and data integrity of the model
 */
class LoanApplicationTest {

    private LoanApplication loanApplication;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        borrower = new Borrower();
        borrower.setFirstName("John");
        borrower.setLastName("Doe");
        borrower.setDob(LocalDate.of(1990, 1, 1));
        borrower.setEmploymentStatus("Employed");
        borrower.setAnnualIncome(55000.0);

        loanApplication = new LoanApplication();
    }

    @Test
    void createLoanApplication_WithValidData_ShouldSetAllFields() {
        // Act
        loanApplication.setBorrower(borrower);
        loanApplication.setLoanAmount(25000.0);
        loanApplication.setLoanType("Personal");
        loanApplication.setTermMonths(36);
        loanApplication.setRiskScore(75.5);
        loanApplication.setRiskGrade("Low");
        loanApplication.setDecision("Approve");

        // Assert
        assertEquals(borrower, loanApplication.getBorrower());
        assertEquals(25000.0, loanApplication.getLoanAmount());
        assertEquals("Personal", loanApplication.getLoanType());
        assertEquals(36, loanApplication.getTermMonths());
        assertEquals(75.5, loanApplication.getRiskScore());
        assertEquals("Low", loanApplication.getRiskGrade());
        assertEquals("Approve", loanApplication.getDecision());
    }

    @Test
    void loanApplication_WithNullBorrower_ShouldAllowNull() {
        // Act
        loanApplication.setBorrower(null);

        // Assert
        assertNull(loanApplication.getBorrower());
    }

    @Test
    void loanApplication_WithZeroLoanAmount_ShouldAcceptValue() {
        // Act
        loanApplication.setLoanAmount(0.0);

        // Assert
        assertEquals(0.0, loanApplication.getLoanAmount());
    }

    @Test
    void loanApplication_WithNegativeLoanAmount_ShouldAcceptValue() {
        // Act
        loanApplication.setLoanAmount(-1000.0);

        // Assert
        assertEquals(-1000.0, loanApplication.getLoanAmount());
    }

    @Test
    void loanApplication_RiskScoreBoundaries_ShouldAcceptAnyValue() {
        loanApplication.setRiskScore(0.0);
        assertEquals(0.0, loanApplication.getRiskScore());

        loanApplication.setRiskScore(100.0);
        assertEquals(100.0, loanApplication.getRiskScore());

        loanApplication.setRiskScore(-10.0);
        assertEquals(-10.0, loanApplication.getRiskScore());

        loanApplication.setRiskScore(150.0);
        assertEquals(150.0, loanApplication.getRiskScore());
    }

    @Test
    void loanApplication_RiskGrades_ShouldAcceptValidGrades() {
        String[] validGrades = {"Low", "Medium", "High"};
        
        for (String grade : validGrades) {
            loanApplication.setRiskGrade(grade);
            assertEquals(grade, loanApplication.getRiskGrade());
        }
    }

    @Test
    void loanApplication_Decisions_ShouldAcceptValidDecisions() {
        String[] validDecisions = {"Approve", "Reject"};
        
        for (String decision : validDecisions) {
            loanApplication.setDecision(decision);
            assertEquals(decision, loanApplication.getDecision());
        }
    }

    @Test
    void loanApplication_WithCompleteData_ShouldRepresentTypicalLoan() {
        // Arrange & Act
        loanApplication.setBorrower(borrower);
        loanApplication.setLoanAmount(50000.0);
        loanApplication.setLoanType("Auto");
        loanApplication.setTermMonths(60);
        loanApplication.setRiskScore(82.5);
        loanApplication.setRiskGrade("Low");
        loanApplication.setDecision("Approve");

        // Assert
        assertNotNull(loanApplication.getBorrower());
        assertEquals("John", loanApplication.getBorrower().getFirstName());
        assertEquals("Doe", loanApplication.getBorrower().getLastName());
        assertEquals(50000.0, loanApplication.getLoanAmount());
        assertEquals("Auto", loanApplication.getLoanType());
        assertEquals(60, loanApplication.getTermMonths());
        assertEquals(82.5, loanApplication.getRiskScore());
        assertEquals("Low", loanApplication.getRiskGrade());
        assertEquals("Approve", loanApplication.getDecision());
    }
}