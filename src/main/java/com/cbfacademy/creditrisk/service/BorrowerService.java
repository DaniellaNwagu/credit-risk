package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Borrower operations.
 * Centralizes business logic such as create, read, update, and delete.
 * Keeps the controller thin and maintains separation of concerns.
 */
@Service
public class BorrowerService {

    private final BorrowerRepository repository;

    public BorrowerService(BorrowerRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new Borrower.
     * @param borrower Borrower entity to persist
     * @return persisted Borrower
     */
    public Borrower createBorrower(Borrower borrower) {
        // Optional: add validation here (e.g., DOB in past, positive income)
        validateBorrower(borrower);
        return repository.save(borrower);
    }

    /**
     * Retrieve all Borrowers.
     */
    public List<Borrower> getAllBorrowers() {
        return repository.findAll();
    }

    /**
     * Retrieve a Borrower by ID.
     */
    public Borrower getBorrowerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
    }

    /**
     * Update an existing Borrower.
     * @param borrower Borrower entity with updated fields
     * @return updated Borrower
     */
    public Borrower updateBorrower(Borrower borrower) {
        Borrower existing = repository.findById(borrower.getId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        existing.setFirstName(borrower.getFirstName());
        existing.setLastName(borrower.getLastName());
        existing.setDob(borrower.getDob());
        existing.setEmploymentStatus(borrower.getEmploymentStatus());
        existing.setAnnualIncome(borrower.getAnnualIncome());

        validateBorrower(existing); // Optional validation
        return repository.save(existing);
    }

    /**
     * Delete a Borrower by ID.
     */
    public void deleteBorrower(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Borrower not found");
        }
        repository.deleteById(id);
    }

    /**
     * Optional validation logic for Borrower.
     */
    private void validateBorrower(Borrower borrower) {
        if (borrower.getAnnualIncome() < 0) {
            throw new RuntimeException("Annual income cannot be negative");
        }
        if (borrower.getDob() == null) {
            throw new RuntimeException("Date of birth is required");
        }
        // Additional rules can be added here
    }
}
