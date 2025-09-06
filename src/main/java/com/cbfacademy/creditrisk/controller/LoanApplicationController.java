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
 * Handles all CRUD operations for loans, including risk grading and decision-making.
 * Uses DTOs to decouple API contract from persistence layer.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    private final LoanApplicationService service;
    private final BorrowerRepository borrowerRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoanApplicationController(LoanApplicationService service,
                                     BorrowerRepository borrowerRepository,
                                     LoanApplicationRepository loanApplicationRepository) {
        this.service = service;
        this.borrowerRepository = borrowerRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    /**
     * Create a new LoanApplication.
     * @param request LoanApplicationRequest DTO with borrowerId and loan details
     * @return LoanApplicationResponse DTO with persisted loan and risk decision
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
     * Retrieve all LoanApplications or filter by risk grade.
     * @param riskGrade Optional query parameter for filtering
     * @return List of LoanApplicationResponse DTOs
     */
    @GetMapping
    public List<LoanApplicationResponse> getLoans(@RequestParam(required = false) String riskGrade) {
        List<LoanApplication> loans = (riskGrade != null)
                ? service.getLoansByRiskGrade(riskGrade)
                : service.getLoansByRiskGrade("");

        return loans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single LoanApplication by ID.
     * @param id LoanApplication ID
     * @return LoanApplicationResponse DTO
     */
    @GetMapping("/{id}")
    public LoanApplicationResponse getLoan(@PathVariable Long id) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        return mapToResponse(loan);
    }

    /**
     * Update an existing LoanApplication.
     * @param id LoanApplication ID
     * @param request LoanApplicationRequest DTO with updated info
     * @return Updated LoanApplicationResponse DTO
     */
    @PutMapping("/{id}")
    public LoanApplicationResponse updateLoan(@PathVariable Long id,
                                              @RequestBody LoanApplicationRequest request) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getBorrower().getId().equals(request.getBorrowerId())) {
            Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                    .orElseThrow(() -> new RuntimeException("Borrower not found"));
            loan.setBorrower(borrower);
        }

        loan.setLoanAmount(request.getLoanAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setLoanType(request.getLoanType());

        LoanApplication updated = service.updateLoanApplication(loan);
        return mapToResponse(updated);
    }

    /**
     * Delete a LoanApplication by ID.
     * @param id LoanApplication ID
     */
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanApplicationRepository.deleteById(id);
    }

    /**
     * Helper method to map LoanApplication entity to LoanApplicationResponse DTO.
     * @param loan LoanApplication entity
     * @return LoanApplicationResponse DTO
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
        return response;
    }
}
