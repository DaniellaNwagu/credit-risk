package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for LoanApplication operations.
 * Handles creation, update, and retrieval of loans.
 * Includes risk scoring, risk grade assignment, and decision logic.
 */
@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new LoanApplication.
     * Performs risk scoring, determines risk grade, and sets decision.
     * @param loan LoanApplication entity to create
     * @return Persisted LoanApplication with risk data
     */
    public LoanApplication createLoanApplication(LoanApplication loan) {
        applyRiskScoring(loan);
        return repository.save(loan);
    }

    /**
     * Update an existing LoanApplication.
     * Recalculates risk score, risk grade, and decision if relevant fields changed.
     * @param loan LoanApplication entity with updated data
     * @return Persisted LoanApplication with updated risk data
     */
    public LoanApplication updateLoanApplication(LoanApplication loan) {
        applyRiskScoring(loan);
        return repository.save(loan);
    }

    /**
     * Retrieve loans filtered by risk grade.
     * If riskGrade is empty or null, returns all loans.
     * @param riskGrade Risk grade filter (e.g., "Low", "Medium", "High")
     * @return List of LoanApplications matching the risk grade
     */
    public List<LoanApplication> getLoansByRiskGrade(String riskGrade) {
        if (riskGrade == null || riskGrade.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByRiskGrade(riskGrade);
    }

    /**
     * Retrieve a LoanApplication by ID.
     * @param id LoanApplication ID
     * @return LoanApplication entity
     */
    public LoanApplication getLoanById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    /**
     * Delete a LoanApplication by ID.
     * @param id LoanApplication ID to delete
     */
    public void deleteLoan(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Loan not found");
        }
        repository.deleteById(id);
    }

    /**
     * Private helper method to calculate risk score, risk grade, and decision.
     * Encapsulates business logic for risk evaluation.
     * @param loan LoanApplication entity to evaluate
     */
    private void applyRiskScoring(LoanApplication loan) {
        // Example risk scoring: higher loan relative to 10k reduces score
        double riskScore = 100 - (loan.getLoanAmount() / 10000);
        loan.setRiskScore(Math.max(Math.min(riskScore, 100), 0)); // Clamp between 0-100

        // Determine risk grade
        if (riskScore >= 75) loan.setRiskGrade("Low");
        else if (riskScore >= 50) loan.setRiskGrade("Medium");
        else loan.setRiskGrade("High");

        // Set decision based on risk grade
        loan.setDecision(loan.getRiskGrade().equals("High") ? "Reject" : "Approve");
    }
}
