package com.cbfacademy.creditrisk.controller;

import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.service.BorrowerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for Borrower endpoints.
 * Supports full CRUD operations and filtering by last name.
 */
@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService service;

    public BorrowerController(BorrowerService service) {
        this.service = service;
    }

    @PostMapping
    public Borrower createBorrower(@RequestBody Borrower borrower) {
        return service.createBorrower(borrower); // Handle creation
    }

    @GetMapping("/{id}")
    public Borrower getBorrower(@PathVariable Long id) {
        return service.getBorrower(id); // Retrieve by ID
    }

    @GetMapping
    public List<Borrower> searchBorrowers(@RequestParam(required = false) String lastName) {
        // Conditional filtering by last name
        if (lastName != null) return service.getBorrowersByLastName(lastName);
        return service.getBorrowersByLastName(""); // Return all if no filter
    }

    @PutMapping("/{id}")
    public Borrower updateBorrower(@PathVariable Long id, @RequestBody Borrower borrower) {
        return service.updateBorrower(id, borrower); // Update existing borrower
    }

    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        service.deleteBorrower(id); // Delete borrower safely
    }
}
