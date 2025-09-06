package com.cbfacademy.creditrisk;

import com.cbfacademy.creditrisk.dto.BorrowerRequest;
import com.cbfacademy.creditrisk.dto.BorrowerResponse;
import com.cbfacademy.creditrisk.dto.LoanApplicationRequest;
import com.cbfacademy.creditrisk.dto.LoanApplicationResponse;
import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for CreditRisk application.
 * Covers Borrower CRUD and Loan creation with risk scoring.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CreditRiskIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BorrowerRepository borrowerRepository;
    @Autowired private LoanApplicationRepository loanRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        // Clear all records before each test
        loanRepository.deleteAll();
        borrowerRepository.deleteAll();
    }

    @Test
    void testBorrowerCrudAndLoanRisk() throws Exception {
        // --- Step 1: Create Borrower ---
        BorrowerRequest borrowerRequest = new BorrowerRequest();
        borrowerRequest.setFirstName("Daniella");
        borrowerRequest.setLastName("Nwagu");
        borrowerRequest.setDob(LocalDate.of(1995, 5, 15));
        borrowerRequest.setEmploymentStatus("Employed");
        borrowerRequest.setAnnualIncome(50000.0);

        String borrowerResponseJson = mockMvc.perform(post("/api/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        BorrowerResponse createdBorrower = objectMapper.readValue(borrowerResponseJson, BorrowerResponse.class);

        // --- Step 2: Retrieve Borrower ---
        mockMvc.perform(get("/api/borrowers/{id}", createdBorrower.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Nwagu"));

        // --- Step 3: Create Loan ---
        LoanApplicationRequest loanRequest = new LoanApplicationRequest();
        loanRequest.setBorrowerId(createdBorrower.getId());
        loanRequest.setLoanAmount(20000.0);
        loanRequest.setTermMonths(24);
        loanRequest.setLoanType("Personal");

        String loanResponseJson = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.riskGrade").exists())
                .andExpect(jsonPath("$.decision").exists())
                .andReturn().getResponse().getContentAsString();

        LoanApplicationResponse createdLoan = objectMapper.readValue(loanResponseJson, LoanApplicationResponse.class);

        // --- Step 4: Retrieve Loan by ID ---
        mockMvc.perform(get("/api/loans/{id}", createdLoan.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanAmount").value(20000.0))
                .andExpect(jsonPath("$.riskGrade").exists())
                .andExpect(jsonPath("$.decision").exists());
    }
}
