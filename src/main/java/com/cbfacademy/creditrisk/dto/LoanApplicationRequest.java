package com.cbfacademy.creditrisk.dto;

public class LoanApplicationRequest {
    private Long borrowerId;      // ID of existing borrower
    private Double loanAmount;
    private Integer termMonths;
    private String loanType;

    // Getters
    public Long getBorrowerId() { 
        return borrowerId; 
    }
    public Double getLoanAmount() { 
        return loanAmount; 
    }
    public Integer getTermMonths() { 
        return termMonths; 
    }
    public String getLoanType() { 
        return loanType; 
    }

    // Setters
    public void setBorrowerId(Long borrowerId) { 
        this.borrowerId = borrowerId;
     }
    public void setLoanAmount(Double loanAmount) { 
        this.loanAmount = loanAmount;
     }
    public void setTermMonths(Integer termMonths) { 
        this.termMonths = termMonths; 
    }
    public void setLoanType(String loanType) { 
        this.loanType = loanType; 
    }
}
