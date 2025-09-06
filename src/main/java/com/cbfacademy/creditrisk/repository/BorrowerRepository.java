package com.cbfacademy.creditrisk.repository;

import com.cbfacademy.creditrisk.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Borrower entity.
 * Includes derived queries for filtering.
 */
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    // Partial match search by last name
    List<Borrower> findByLastNameContainingIgnoreCase(String lastName);

    // Full match search for a single borrower by first name, last name, and DOB
    Optional<Borrower> findByFirstNameAndLastNameAndDob(String firstName, String lastName, LocalDate dob);
}
