package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for LoanApplication operations.
 * Includes risk scoring and decision logic.
 */
@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    public LoanApplication createLoanApplication(LoanApplication loan) {
        // Example risk scoring: higher loan relative to 10k reduces score
        double riskScore = 100 - (loan.getLoanAmount() / 10000); 
        loan.setRiskScore(Math.max(Math.min(riskScore, 100), 0)); // Clamp between 0-100

        // Determine risk grade
        if (riskScore >= 75) loan.setRiskGrade("Low");
        else if (riskScore >= 50) loan.setRiskGrade("Medium");
        else loan.setRiskGrade("High");

        // Set decision based on risk grade
        loan.setDecision(loan.getRiskGrade().equals("High") ? "Reject" : "Approve");

        return repository.save(loan); // Persist loan application
    }

    public List<LoanApplication> getLoansByRiskGrade(String riskGrade) {
        return repository.findByRiskGrade(riskGrade); // Filter by risk grade
    }
}
