package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.dto.BorrowerRequest;
import com.cbfacademy.creditrisk.dto.BorrowerResponse;
import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.service.BorrowerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for Borrower-related endpoints.
 * Follows proper layered architecture by using service layer for business logic.
 * Uses DTOs to decouple API contract from entity persistence.
 */
@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    /**
     * Create a new Borrower.
     * @param request BorrowerRequest DTO containing input data
     * @return BorrowerResponse DTO with saved borrower details
     */
    @PostMapping
    public BorrowerResponse createBorrower(@RequestBody BorrowerRequest request) {
        Borrower borrower = mapToEntity(request);
        Borrower saved = borrowerService.createBorrower(borrower);
        return mapToResponse(saved);
    }

    /**
     * Retrieve a Borrower by ID.
     */
    @GetMapping("/{id}")
    public BorrowerResponse getBorrower(@PathVariable Long id) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        return mapToResponse(borrower);
    }

    /**
     * Retrieve all Borrowers.
     */
    @GetMapping
    public List<BorrowerResponse> getAllBorrowers() {
        return borrowerService.getAllBorrowers()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing Borrower.
     */
    @PutMapping("/{id}")
    public BorrowerResponse updateBorrower(@PathVariable Long id, @RequestBody BorrowerRequest request) {
        Borrower borrower = mapToEntity(request);
        Borrower updated = borrowerService.updateBorrower(id, borrower);
        return mapToResponse(updated);
    }

    /**
     * Delete a Borrower by ID.
     */
    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
    }

    /**
     * Map BorrowerRequest DTO to Borrower entity.
     */
    private Borrower mapToEntity(BorrowerRequest request) {
        Borrower borrower = new Borrower();
        borrower.setFirstName(request.getFirstName());
        borrower.setLastName(request.getLastName());
        borrower.setDob(request.getDob());
        borrower.setEmploymentStatus(request.getEmploymentStatus());
        borrower.setAnnualIncome(request.getAnnualIncome());
        return borrower;
    }

    /**
     * Map Borrower entity to BorrowerResponse DTO.
     */
    private BorrowerResponse mapToResponse(Borrower borrower) {
        BorrowerResponse response = new BorrowerResponse();
        response.setId(borrower.getId());
        response.setFirstName(borrower.getFirstName());
        response.setLastName(borrower.getLastName());
        response.setDob(borrower.getDob());
        response.setEmploymentStatus(borrower.getEmploymentStatus());
        response.setAnnualIncome(borrower.getAnnualIncome());
        return response;
    }
}
