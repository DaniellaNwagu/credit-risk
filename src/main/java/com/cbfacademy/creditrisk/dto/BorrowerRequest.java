package com.cbfacademy.creditrisk.dto;

import java.time.LocalDate;

public class BorrowerRequest {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String employmentStatus;
    private Double annualIncome;

    // Getters
    public String getFirstName() {
         return firstName; 
        }
    public String getLastName() { 
        return lastName;
     }
    public LocalDate getDob() {
         return dob; 
        }
    public String getEmploymentStatus() { 
        return employmentStatus;
     }
    public Double getAnnualIncome() { 
        return annualIncome; 
    }

    // Setters
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }
    public void setDob(LocalDate dob) { 
        this.dob = dob; 
    }
    public void setEmploymentStatus(String employmentStatus) { 
        this.employmentStatus = employmentStatus;
     }
    public void setAnnualIncome(Double annualIncome) { 
        this.annualIncome = annualIncome;
     }
}
