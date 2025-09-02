package com.cbfacademy.creditrisk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Borrower entity stores borrower information.
 * Inherits auditing fields from BaseEntity.
 */
@Entity
@Table(name = "borrowers")
public class Borrower extends BaseEntity {

    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String ssnNin; // Sensitive, consider encryption in production
    private String employmentStatus;
    private Double annualIncome;

    // Getters 
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public LocalDate getDob(){
        return dob;
    }
    public String getSsnNin(){
        return ssnNin;
    }
    public String getEmploymentStatus(){
        return employmentStatus;
    }
    public Double getAnnualIncome(){
        return annualIncome;
    }

    //Setters
    public void setFirstName(String firstName) {
         this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public void setSsnNin(String ssnNin) {
        this.ssnNin = ssnNin;
    }
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    public void setAnnualIncome(Double annualIncome){
        this.annualIncome = annualIncome;
    }

}
