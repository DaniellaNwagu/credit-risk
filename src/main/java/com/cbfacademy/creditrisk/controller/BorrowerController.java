package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.dto.BorrowerRequest;
import com.cbfacademy.creditrisk.dto.BorrowerResponse;
import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for Borrower endpoints.
 * Handles all CRUD operations (Create, Read, Update, Delete) for Borrowers.
 * Uses DTOs to decouple API contracts from persistence layer.
 */
@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerRepository borrowerRepository;

    public BorrowerController(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    /**
     * Create a new Borrower.
     * @param request BorrowerRequest DTO containing borrower information
     * @return BorrowerResponse DTO containing persisted borrower information
     */
    @PostMapping
    public BorrowerResponse createBorrower(@RequestBody BorrowerRequest request) {
        Borrower borrower = new Borrower();
        borrower.setFirstName(request.getFirstName());
        borrower.setLastName(request.getLastName());
        borrower.setDob(request.getDob());
        borrower.setEmploymentStatus(request.getEmploymentStatus());
        borrower.setAnnualIncome(request.getAnnualIncome());

        Borrower saved = borrowerRepository.save(borrower);
        return mapToResponse(saved);
    }

    /**
     * Retrieve a Borrower by ID.
     * @param id Borrower ID
     * @return BorrowerResponse DTO with borrower details
     */
    @GetMapping("/{id}")
    public BorrowerResponse getBorrower(@PathVariable Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        return mapToResponse(borrower);
    }

    /**
     * Retrieve all Borrowers.
     * @return List of BorrowerResponse DTOs
     */
    @GetMapping
    public List<BorrowerResponse> getAllBorrowers() {
        return borrowerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing Borrower.
     * @param id Borrower ID
     * @param request BorrowerRequest DTO with updated information
     * @return Updated BorrowerResponse DTO
     */
    @PutMapping("/{id}")
    public BorrowerResponse updateBorrower(@PathVariable Long id, @RequestBody BorrowerRequest request) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        borrower.setFirstName(request.getFirstName());
        borrower.setLastName(request.getLastName());
        borrower.setDob(request.getDob());
        borrower.setEmploymentStatus(request.getEmploymentStatus());
        borrower.setAnnualIncome(request.getAnnualIncome());

        Borrower updated = borrowerRepository.save(borrower);
        return mapToResponse(updated);
    }

    /**
     * Delete a Borrower by ID.
     * @param id Borrower ID
     */
    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        borrowerRepository.deleteById(id);
    }

    /**
     * Helper method to map Borrower entity to BorrowerResponse DTO.
     * @param borrower Borrower entity
     * @return BorrowerResponse DTO
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
