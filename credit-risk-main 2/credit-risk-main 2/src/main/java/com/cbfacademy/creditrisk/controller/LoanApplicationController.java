package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.dto.LoanApplicationRequest;
import com.cbfacademy.creditrisk.dto.LoanApplicationResponse;
import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.service.BorrowerService;
import com.cbfacademy.creditrisk.service.LoanApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for LoanApplication endpoints.
 * Follows proper layered architecture by using service layers for business logic.
 * Uses DTOs to decouple API contract from persistence layer.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final BorrowerService borrowerService;

    public LoanApplicationController(LoanApplicationService loanApplicationService, BorrowerService borrowerService) {
        this.loanApplicationService = loanApplicationService;
        this.borrowerService = borrowerService;
    }

    /**
     * Create a new LoanApplication.
     */
    @PostMapping
    public LoanApplicationResponse createLoan(@RequestBody LoanApplicationRequest request) {
        Borrower borrower = borrowerService.getBorrowerById(request.getBorrowerId());

        LoanApplication loan = new LoanApplication();
        loan.setBorrower(borrower);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setLoanType(request.getLoanType());

        LoanApplication saved = loanApplicationService.createLoanApplication(loan);
        return mapToResponse(saved);
    }

    /**
     * Retrieve a single LoanApplication by its ID.
     * Used to check the status, risk score, and decision of a specific loan.
     */
    @GetMapping("/{id}")
    public LoanApplicationResponse getLoanById(@PathVariable Long id) {
        LoanApplication loan = loanApplicationService.getLoanById(id);
        return mapToResponse(loan);
    }

    /**
     * Retrieve loans, optionally filtered by risk grade.
     */
    @GetMapping
    public List<LoanApplicationResponse> getLoans(@RequestParam(required = false) String riskGrade) {
        List<LoanApplication> loans = loanApplicationService.getLoansByRiskGrade(riskGrade);
        return loans.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Update an existing LoanApplication.
     */
    @PutMapping("/{id}")
    public LoanApplicationResponse updateLoan(@PathVariable Long id, @RequestBody LoanApplicationRequest request) {
        LoanApplication loan = loanApplicationService.getLoanById(id);
        Borrower borrower = borrowerService.getBorrowerById(request.getBorrowerId());

        loan.setBorrower(borrower);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setLoanType(request.getLoanType());

        LoanApplication updated = loanApplicationService.updateLoanApplication(loan);
        return mapToResponse(updated);
    }

    /**
     * Delete a LoanApplication by ID.
     */
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanApplicationService.deleteLoan(id);
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
