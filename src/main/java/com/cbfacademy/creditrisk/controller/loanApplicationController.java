package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.service.LoanApplicationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for LoanApplication endpoints.
 * Handles creation and filtered retrieval.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    // Create loan with risk scoring
    @PostMapping
    public LoanApplication createLoan(@RequestBody LoanApplication loan) {
        return service.createLoanApplication(loan); 
    }

    // Retrieve loans filtered by risk grade, or all if not provided
    @GetMapping
    public List<LoanApplication> getLoans(@RequestParam(required = false) String riskGrade) {
        if (riskGrade != null) return service.getLoansByRiskGrade(riskGrade);
        return service.getLoansByRiskGrade("");
    }
  
}
