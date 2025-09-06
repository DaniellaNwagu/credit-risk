package com.cbfacademy.creditrisk.dto;

public class LoanApplicationResponse {
    private Long id;
    private Long borrowerId;
    private Double loanAmount;
    private Integer termMonths;
    private String loanType;
    private String riskGrade;
    private String decision;

    // Getters & Setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public Long getBorrowerId() { 
        return borrowerId;
     }
    public void setBorrowerId(Long borrowerId) { 
        this.borrowerId = borrowerId; 
    }

    public Double getLoanAmount() { 
        return loanAmount; 
    }
    public void setLoanAmount(Double loanAmount) { 
        this.loanAmount = loanAmount;
     }

    public Integer getTermMonths() { 
        return termMonths; 
    }
    public void setTermMonths(Integer termMonths) { 
        this.termMonths = termMonths; 
    }

    public String getLoanType() { 
        return loanType; 
    }
    public void setLoanType(String loanType) { 
        this.loanType = loanType; 
    }

    public String getRiskGrade() {
         return riskGrade; 
        }
    public void setRiskGrade(String riskGrade) { 
        this.riskGrade = riskGrade; 
    }

    public String getDecision() { 
        return decision; 
    }
    public void setDecision(String decision) {
         this.decision = decision; 
        }
}
