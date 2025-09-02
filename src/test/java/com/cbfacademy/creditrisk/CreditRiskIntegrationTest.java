package com.cbfacademy.creditrisk;


import com.cbfacademy.creditrisk.model.Borrower;
import com.cbfacademy.creditrisk.model.LoanApplication;
import com.cbfacademy.creditrisk.repository.BorrowerRepository;
import com.cbfacademy.creditrisk.repository.LoanApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CreditRiskIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BorrowerRepository borrowerRepository;
    @Autowired private LoanApplicationRepository loanRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        borrowerRepository.deleteAll();
    }

    @Test
    void testBorrowerCrudAndLoanRisk() throws Exception {
        // --- Step 1: Create Borrower ---
        Borrower borrower = new Borrower();
        borrower.setFirstName("Daniella");
        borrower.setLastName("Nwagu");
        borrower.setDob(LocalDate.of(1995, 5, 15));
        borrower.setEmploymentStatus("Employed");
        borrower.setAnnualIncome(50000.0);

        String borrowerResponse = mockMvc.perform(post("/api/borrowers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(borrower)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Borrower createdBorrower = objectMapper.readValue(borrowerResponse, Borrower.class);

        // --- Step 2: Retrieve Borrower ---
        mockMvc.perform(get("/api/borrowers/{id}", createdBorrower.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Nwagu"));

        // --- Step 3: Create LoanApplication ---
        LoanApplication loan = new LoanApplication();
        loan.setBorrower(createdBorrower);
        loan.setLoanAmount(20000.0);
        loan.setLoanTermMonths(24);
        loan.setLoanType("Personal");

        String loanResponse = mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.riskGrade").exists())
                .andExpect(jsonPath("$.decision").exists())
                .andReturn().getResponse().getContentAsString();

        LoanApplication createdLoan = objectMapper.readValue(loanResponse, LoanApplication.class);

        // --- Step 4: Retrieve Loan by ID ---
        mockMvc.perform(get("/api/loans/{id}", createdLoan.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanAmount").value(20000.0))
                .andExpect(jsonPath("$.riskGrade").exists())
                .andExpect(jsonPath("$.decision").exists());
    }
}

