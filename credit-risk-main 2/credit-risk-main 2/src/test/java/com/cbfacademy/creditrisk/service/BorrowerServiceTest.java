package com.cbfacademy.creditrisk.service;

import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
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
 * Unit tests for BorrowerService
 * Tests main business logic including validation and CRUD operations
 */
@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    private Borrower testBorrower;

    @BeforeEach
    void setUp() {
        testBorrower = new Borrower();
        testBorrower.setFirstName("John");
        testBorrower.setLastName("Doe");
        testBorrower.setDob(LocalDate.of(1990, 1, 1));
        testBorrower.setEmploymentStatus("Employed");
        testBorrower.setAnnualIncome(55000.0);
    }

    @Test
    void createBorrower_ValidBorrower_ShouldReturnSavedBorrower() {
        // Arrange
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(testBorrower);

        // Act
        Borrower result = borrowerService.createBorrower(testBorrower);

        // Assert
        assertEquals(testBorrower.getFirstName(), result.getFirstName());
        assertEquals(testBorrower.getLastName(), result.getLastName());
        assertEquals(testBorrower.getAnnualIncome(), result.getAnnualIncome());
        verify(borrowerRepository, times(1)).save(testBorrower);
    }

    @Test
    void createBorrower_NegativeIncome_ShouldThrowException() {
        // Arrange
        testBorrower.setAnnualIncome(-1000.0);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> borrowerService.createBorrower(testBorrower));
        assertEquals("Annual income cannot be negative", exception.getMessage());
        verify(borrowerRepository, never()).save(any());
    }

    @Test
    void createBorrower_NullDateOfBirth_ShouldThrowException() {
        // Arrange
        testBorrower.setDob(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> borrowerService.createBorrower(testBorrower));
        assertEquals("Date of birth is required", exception.getMessage());
        verify(borrowerRepository, never()).save(any());
    }

    @Test
    void getAllBorrowers_ShouldReturnAllBorrowers() {
        // Arrange
        Borrower borrower2 = new Borrower();
        borrower2.setFirstName("Jane");
        borrower2.setLastName("Smith");
        List<Borrower> expectedBorrowers = Arrays.asList(testBorrower, borrower2);
        when(borrowerRepository.findAll()).thenReturn(expectedBorrowers);

        // Act
        List<Borrower> result = borrowerService.getAllBorrowers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedBorrowers, result);
        verify(borrowerRepository, times(1)).findAll();
    }

    @Test
    void getBorrowerById_ExistingId_ShouldReturnBorrower() {
        // Arrange
        Long borrowerId = 1L;
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(testBorrower));

        // Act
        Borrower result = borrowerService.getBorrowerById(borrowerId);

        // Assert
        assertEquals(testBorrower, result);
        verify(borrowerRepository, times(1)).findById(borrowerId);
    }

    @Test
    void getBorrowerById_NonExistingId_ShouldThrowException() {
        // Arrange
        Long borrowerId = 999L;
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> borrowerService.getBorrowerById(borrowerId));
        assertEquals("Borrower not found", exception.getMessage());
        verify(borrowerRepository, times(1)).findById(borrowerId);
    }

    @Test
    void updateBorrower_ExistingBorrower_ShouldReturnUpdatedBorrower() {
        // Arrange
        Long borrowerId = 1L;
        
        Borrower existingBorrower = new Borrower();
        existingBorrower.setFirstName("OldName");
        existingBorrower.setLastName("OldLastName");
        existingBorrower.setDob(LocalDate.of(1985, 1, 1));
        existingBorrower.setEmploymentStatus("Unemployed");
        existingBorrower.setAnnualIncome(30000.0);
        
        when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(existingBorrower));
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(existingBorrower);

        // Act
        Borrower result = borrowerService.updateBorrower(borrowerId, testBorrower);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Employed", result.getEmploymentStatus());
        assertEquals(55000.0, result.getAnnualIncome());
        verify(borrowerRepository, times(1)).findById(borrowerId);
        verify(borrowerRepository, times(1)).save(existingBorrower);
    }

    @Test
    void deleteBorrower_ExistingId_ShouldDeleteBorrower() {
        // Arrange
        Long borrowerId = 1L;
        when(borrowerRepository.existsById(borrowerId)).thenReturn(true);

        // Act
        borrowerService.deleteBorrower(borrowerId);

        // Assert
        verify(borrowerRepository, times(1)).existsById(borrowerId);
        verify(borrowerRepository, times(1)).deleteById(borrowerId);
    }

    @Test
    void deleteBorrower_NonExistingId_ShouldThrowException() {
        // Arrange
        Long borrowerId = 999L;
        when(borrowerRepository.existsById(borrowerId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> borrowerService.deleteBorrower(borrowerId));
        assertEquals("Borrower not found", exception.getMessage());
        verify(borrowerRepository, times(1)).existsById(borrowerId);
        verify(borrowerRepository, never()).deleteById(borrowerId);
    }
}