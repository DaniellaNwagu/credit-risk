package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LoanApplicationService
 * Tests main business logic including risk scoring algorithm and decision making
 */
@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    private LoanApplication testLoanApplication;
    private Borrower testBorrower;

    @BeforeEach
    void setUp() {
        testBorrower = new Borrower();
        testBorrower.setFirstName("John");
        testBorrower.setLastName("Doe");
        testBorrower.setDob(LocalDate.of(1990, 1, 1));
        testBorrower.setEmploymentStatus("Employed");
        testBorrower.setAnnualIncome(55000.0);

        testLoanApplication = new LoanApplication();
        testLoanApplication.setBorrower(testBorrower);
        testLoanApplication.setLoanAmount(20000.0);
        testLoanApplication.setTermMonths(24);
        testLoanApplication.setLoanType("Personal");
    }

    @Test
    void createLoanApplication_SmallLoanAmount_ShouldHaveLowRiskAndApprove() {
        // Arrange
        testLoanApplication.setLoanAmount(5000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);

        // Assert
        assertEquals(99.5, result.getRiskScore(), 0.1);
        assertEquals("Low", result.getRiskGrade());
        assertEquals("Approve", result.getDecision());
        verify(loanApplicationRepository, times(1)).save(testLoanApplication);
    }

    @Test
    void createLoanApplication_MediumLoanAmount_ShouldHaveMediumRiskAndApprove() {
        // Arrange
        testLoanApplication.setLoanAmount(30000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);
        
        // Assert
        assertTrue(result.getRiskScore() >= 75);
        assertEquals("Low", result.getRiskGrade());
        assertEquals("Approve", result.getDecision());
    }

    @Test
    void createLoanApplication_LargeLoanAmount_ShouldHaveHighRiskAndReject() {
        // Arrange
        testLoanApplication.setLoanAmount(600000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);

        // Assert
        assertEquals(40.0, result.getRiskScore(), 0.1);
        assertEquals("High", result.getRiskGrade());
        assertEquals("Reject", result.getDecision());
        verify(loanApplicationRepository, times(1)).save(testLoanApplication);
    }

    @Test
    void createLoanApplication_ExtremelyLargeLoan_ShouldClampRiskScoreToZero() {
        // Arrange
        testLoanApplication.setLoanAmount(2000000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);

        // Assert
        assertEquals(0.0, result.getRiskScore());
        assertEquals("High", result.getRiskGrade());
        assertEquals("Reject", result.getDecision());
    }

    @Test
    void createLoanApplication_MediumRiskRange_ShouldApprove() {
        // Arrange
        testLoanApplication.setLoanAmount(300000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);

        // Assert
        assertEquals(70.0, result.getRiskScore());
        assertEquals("Medium", result.getRiskGrade());
        assertEquals("Approve", result.getDecision());
    }

    @Test
    void createLoanApplication_EdgeOfHighRisk_ShouldReject() {
        // Arrange
        testLoanApplication.setLoanAmount(501000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.createLoanApplication(testLoanApplication);

        // Assert
        assertEquals(49.9, result.getRiskScore(), 0.1);
        assertEquals("High", result.getRiskGrade());
        assertEquals("Reject", result.getDecision());
    }

    @Test
    void updateLoanApplication_ShouldRecalculateRiskScore() {
        // Arrange
        testLoanApplication.setLoanAmount(100000.0);
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(testLoanApplication);

        // Act
        LoanApplication result = loanApplicationService.updateLoanApplication(testLoanApplication);

        // Assert
        assertEquals(90.0, result.getRiskScore());
        assertEquals("Low", result.getRiskGrade());
        assertEquals("Approve", result.getDecision());
        verify(loanApplicationRepository, times(1)).save(testLoanApplication);
    }

    @Test
    void getLoansByRiskGrade_WithRiskGrade_ShouldReturnFilteredLoans() {
        // Arrange
        String riskGrade = "High";
        LoanApplication loan1 = new LoanApplication();
        LoanApplication loan2 = new LoanApplication();
        List<LoanApplication> expectedLoans = Arrays.asList(loan1, loan2);
        
        when(loanApplicationRepository.findByRiskGrade(riskGrade)).thenReturn(expectedLoans);

        // Act
        List<LoanApplication> result = loanApplicationService.getLoansByRiskGrade(riskGrade);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedLoans, result);
        verify(loanApplicationRepository, times(1)).findByRiskGrade(riskGrade);
    }

    @Test
    void getLoansByRiskGrade_WithNullRiskGrade_ShouldReturnAllLoans() {
        // Arrange
        LoanApplication loan1 = new LoanApplication();
        LoanApplication loan2 = new LoanApplication();
        List<LoanApplication> allLoans = Arrays.asList(loan1, loan2);
        
        when(loanApplicationRepository.findAll()).thenReturn(allLoans);

        // Act
        List<LoanApplication> result = loanApplicationService.getLoansByRiskGrade(null);

        // Assert
        assertEquals(2, result.size());
        assertEquals(allLoans, result);
        verify(loanApplicationRepository, times(1)).findAll();
        verify(loanApplicationRepository, never()).findByRiskGrade(any());
    }

    @Test
    void getLoansByRiskGrade_WithEmptyRiskGrade_ShouldReturnAllLoans() {
        // Arrange
        LoanApplication loan1 = new LoanApplication();
        List<LoanApplication> allLoans = Arrays.asList(loan1);
        
        when(loanApplicationRepository.findAll()).thenReturn(allLoans);

        // Act
        List<LoanApplication> result = loanApplicationService.getLoansByRiskGrade("");

        // Assert
        assertEquals(1, result.size());
        assertEquals(allLoans, result);
        verify(loanApplicationRepository, times(1)).findAll();
        verify(loanApplicationRepository, never()).findByRiskGrade(any());
    }

    @Test
    void getLoanById_ExistingId_ShouldReturnLoan() {
        // Arrange
        Long loanId = 1L;
        testLoanApplication.setLoanAmount(50000.0);
        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(testLoanApplication));

        // Act
        LoanApplication result = loanApplicationService.getLoanById(loanId);

        // Assert
        assertEquals(testLoanApplication, result);
        verify(loanApplicationRepository, times(1)).findById(loanId);
    }

    @Test
    void getLoanById_NonExistingId_ShouldThrowException() {
        // Arrange
        Long loanId = 999L;
        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> loanApplicationService.getLoanById(loanId));
        assertEquals("Loan not found", exception.getMessage());
        verify(loanApplicationRepository, times(1)).findById(loanId);
    }

    @Test
    void deleteLoan_ExistingId_ShouldDeleteSuccessfully() {
        // Arrange
        Long loanId = 1L;
        when(loanApplicationRepository.existsById(loanId)).thenReturn(true);
        doNothing().when(loanApplicationRepository).deleteById(loanId);

        // Act
        loanApplicationService.deleteLoan(loanId);

        // Assert
        verify(loanApplicationRepository, times(1)).existsById(loanId);
        verify(loanApplicationRepository, times(1)).deleteById(loanId);
    }

    @Test
    void deleteLoan_NonExistingId_ShouldThrowException() {
        // Arrange
        Long loanId = 999L;
        when(loanApplicationRepository.existsById(loanId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> loanApplicationService.deleteLoan(loanId));
        assertEquals("Loan not found", exception.getMessage());
        verify(loanApplicationRepository, times(1)).existsById(loanId);
        verify(loanApplicationRepository, never()).deleteById(loanId);
    }
}