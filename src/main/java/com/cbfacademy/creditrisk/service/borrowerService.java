package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import com.cbfacademy.creditrisk.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for Borrower operations.
 * Handles business logic and data access.
 */
@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower createBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower); // Persist new borrower
    }

    public Borrower getBorrower(Long id) {
        // Retrieve borrower, throw exception if not found
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id " + id));
    }

    public List<Borrower> getBorrowersByLastName(String lastName) {
        return borrowerRepository.findByLastNameContainingIgnoreCase(lastName); // Filtered search
    }

    public Borrower updateBorrower(Long id, Borrower updated) {
        Borrower borrower = getBorrower(id); // Validate existence
        // Update key fields
        borrower.setFirstName(updated.getFirstName());
        borrower.setLastName(updated.getLastName());
        borrower.setDob(updated.getDob());
        borrower.setEmploymentStatus(updated.getEmploymentStatus());
        borrower.setAnnualIncome(updated.getAnnualIncome());
        return borrowerRepository.save(borrower); // Save updated record
    }

    public void deleteBorrower(Long id) {
        borrowerRepository.delete(getBorrower(id)); // Safe delete after validation
    }
}
