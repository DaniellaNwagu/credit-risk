package com.cbfacademy.creditrisk.dto;

import java.time.LocalDate;

public class BorrowerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String employmentStatus;
    private Double annualIncome;

    public BorrowerResponse() {}

    // All-args constructor (for easy mapping in test)
    public BorrowerResponse(Long id, String firstName, String lastName, LocalDate dob, String employmentStatus, Double annualIncome) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.employmentStatus = employmentStatus;
        this.annualIncome = annualIncome;
    }
    // Getters & Setters
    public Long getId() { 
        return id;
     }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getFirstName() { 
        return firstName; 
    }
    public void setFirstName(String firstName) {
         this.firstName = firstName;
         }

    public String getLastName() { 
        return lastName;
     }
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public LocalDate getDob() { 
        return dob; 
    }
    public void setDob(LocalDate dob) { 
        this.dob = dob; 
    }

    public String getEmploymentStatus() { 
        return employmentStatus;
     }
    public void setEmploymentStatus(String employmentStatus) { 
        this.employmentStatus = employmentStatus;
     }

    public Double getAnnualIncome() { 
        return annualIncome;
     }
    public void setAnnualIncome(Double annualIncome) { 
        this.annualIncome = annualIncome; 
    }
}
