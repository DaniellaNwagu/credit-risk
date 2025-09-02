package com.cbfacademy.creditrisk.repository;

import com.cbfacademy.creditrisk.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for LoanApplication entity.
 * Includes derived query for filtering by risk grade.
 */
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByRiskGrade(String riskGrade);
}
