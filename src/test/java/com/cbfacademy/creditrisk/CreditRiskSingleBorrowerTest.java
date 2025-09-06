package com.cbfacademy.creditrisk;

import com.cbfacademy.creditrisk.dto.BorrowerRequest;
import com.cbfacademy.creditrisk.dto.BorrowerResponse;
import com.cbfacademy.creditrisk.dto.LoanApplicationRequest;
import com.cbfacademy.creditrisk.dto.LoanApplicationResponse;
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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for CreditRisk application.
 * Focuses on single borrower loan application and API decision.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CreditRiskSingleBorrowerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BorrowerRepository borrowerRepository;
    @Autowired private LoanApplicationRepository loanRepository;
    @Autowired private ObjectMapper objectMapper;

    private final String testFirstName = "John";
    private final String testLastName = "Doe";
    private final LocalDate testDob = LocalDate.of(1990, 1, 1);

    @BeforeEach
    void cleanDatabase() {
        // Clear loans but keep borrowers if you want to test re-use
        loanRepository.deleteAll();
    }

    @Test
    void testSingleBorrowerLoanApplication() throws Exception {
        // --- Step 1: Check if borrower exists ---
        Optional<BorrowerResponse> optionalBorrower = borrowerRepository
                .findByFirstNameAndLastNameAndDob(testFirstName, testLastName, testDob)
                .map(b -> new BorrowerResponse(b.getId(), b.getFirstName(), b.getLastName(), b.getDob(), b.getEmploymentStatus(), b.getAnnualIncome()));

        BorrowerResponse borrower;
        if (optionalBorrower.isPresent()) {
            borrower = optionalBorrower.get();
        } else {
            // --- Step 2: Create borrower if not exists ---
            BorrowerRequest borrowerRequest = new BorrowerRequest();
            borrowerRequest.setFirstName(testFirstName);
            borrowerRequest.setLastName(testLastName);
            borrowerRequest.setDob(testDob);
            borrowerRequest.setEmploymentStatus("Employed");
            borrowerRequest.setAnnualIncome(55000.0);

            String borrowerResponseJson = mockMvc.perform(post("/api/borrowers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(borrowerRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andReturn().getResponse().getContentAsString();

            borrower = objectMapper.readValue(borrowerResponseJson, BorrowerResponse.class);
        }

        // --- Step 3: Submit a single loan application ---
        LoanApplicationRequest loanRequest = new LoanApplicationRequest();
        loanRequest.setBorrowerId(borrower.getId());
        loanRequest.setLoanAmount(20000.0);
        loanRequest.setTermMonths(24);
        loanRequest.setLoanType("Personal");

        String loanResponseJson = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riskScore").isNumber())
                .andExpect(jsonPath("$.riskGrade").isString())
                .andExpect(jsonPath("$.decision").isString())
                .andReturn().getResponse().getContentAsString();

        LoanApplicationResponse loanResponse = objectMapper.readValue(loanResponseJson, LoanApplicationResponse.class);

        // --- Step 4: Validate loan saved in database ---
        mockMvc.perform(get("/api/loans/{id}", loanResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanAmount").value(20000.0))
                .andExpect(jsonPath("$.riskScore").value(loanResponse.getRiskScore()))
                .andExpect(jsonPath("$.riskGrade").value(loanResponse.getRiskGrade()))
                .andExpect(jsonPath("$.decision").value(loanResponse.getDecision()));

        // Print results
        System.out.println("BorrowerId: " + borrower.getId() +
                ", Name: " + borrower.getFirstName() + " " + borrower.getLastName() +
                ", Loan Amount: " + loanRequest.getLoanAmount() +
                ", RiskScore: " + loanResponse.getRiskScore() +
                ", RiskGrade: " + loanResponse.getRiskGrade() +
                ", Decision: " + loanResponse.getDecision());
    }
}
