package com.cbfacademy.creditrisk.repository;

import com.cbfacademy.creditrisk.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Borrower entity.
 * Includes derived query for filtering by last name.
 */
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    List<Borrower> findByLastNameContainingIgnoreCase(String lastName); // Partial match search
}
