package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.dto.LoanApplicationRequest;
import com.cbfacademy.creditrisk.dto.LoanApplicationResponse;
import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import com.cbfacademy.creditrisk.service.LoanApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for LoanApplication endpoints.
 * Handles creation, retrieval, update, and deletion of loans.
 * Uses DTOs to decouple API contract from persistence layer.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    private final LoanApplicationService service;
    private final BorrowerRepository borrowerRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoanApplicationController(LoanApplicationService service, BorrowerRepository borrowerRepository, LoanApplicationRepository loanApplicationRepository) {
        this.service = service;
        this.borrowerRepository = borrowerRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    /**
     * Create a new LoanApplication.
     */
    @PostMapping
    public LoanApplicationResponse createLoan(@RequestBody LoanApplicationRequest request) {
        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        LoanApplication loan = new LoanApplication();
        loan.setBorrower(borrower);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setLoanType(request.getLoanType());

        LoanApplication saved = service.createLoanApplication(loan);
        return mapToResponse(saved);
    }
    /**
     * Retrieve a single LoanApplication by its ID.
     * Used to check the status, risk score, and decision of a specific loan.
     */
    @GetMapping("/{id}")
    public LoanApplicationResponse getLoanById(@PathVariable Long id) {
        LoanApplication loan = loanApplicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Loan not found"));
        return mapToResponse(loan);
}

    /**
     * Retrieve loans, optionally filtered by risk grade.
     */
    @GetMapping
    public List<LoanApplicationResponse> getLoans(@RequestParam(required = false) String riskGrade) {
        List<LoanApplication> loans = service.getLoansByRiskGrade(riskGrade);
        return loans.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Update an existing LoanApplication.
     */
    @PutMapping("/{id}")
    public LoanApplicationResponse updateLoan(@PathVariable Long id, @RequestBody LoanApplicationRequest request) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        loan.setBorrower(borrower);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setLoanType(request.getLoanType());

        LoanApplication updated = service.updateLoanApplication(loan);
        return mapToResponse(updated);
    }

    /**
     * Delete a LoanApplication by ID.
     */
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanApplicationRepository.deleteById(id);
    }

    /**
     * Map LoanApplication entity to LoanApplicationResponse DTO.
     */
    private LoanApplicationResponse mapToResponse(LoanApplication loan) {
    LoanApplicationResponse response = new LoanApplicationResponse();
    response.setId(loan.getId());
    response.setBorrowerId(loan.getBorrower().getId());
    response.setLoanAmount(loan.getLoanAmount());
    response.setTermMonths(loan.getTermMonths());
    response.setLoanType(loan.getLoanType());
    response.setRiskGrade(loan.getRiskGrade());
    response.setDecision(loan.getDecision());
    response.setRiskScore(loan.getRiskScore());  
    return response;
    }

}
