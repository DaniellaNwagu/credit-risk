-- Sample Borrowers
INSERT INTO borrowers (id, first_name, last_name, dob, ssn_nin, employment_status, annual_income, created_at) VALUES
(1, 'John', 'Doe', '1990-05-15', '123-45-6789', 'Employed', 55000.00, NOW()),
(2, 'Jane', 'Smith', '1985-08-22', '987-65-4321', 'Employed', 72000.00, NOW()),
(3, 'Mike', 'Johnson', '1988-03-10', '456-78-9012', 'Self-Employed', 48000.00, NOW()),
(4, 'Sarah', 'Williams', '1992-11-30', '789-01-2345', 'Employed', 65000.00, NOW()),
(5, 'David', 'Brown', '1983-07-18', '345-67-8901', 'Unemployed', 0.00, NOW());

-- Sample Loan Applications
INSERT INTO loan_applications (id, borrower_id, loan_amount, loan_type, term_months, risk_score, risk_grade, decision, created_at) VALUES
(1, 1, 25000.00, 'Personal', 36, 97.5, 'Low', 'Approve', NOW()),
(2, 2, 150000.00, 'Mortgage', 360, 85.0, 'Low', 'Approve', NOW()),
(3, 3, 35000.00, 'Auto', 60, 96.5, 'Low', 'Approve', NOW()),
(4, 4, 80000.00, 'Home Improvement', 120, 92.0, 'Low', 'Approve', NOW()),
(5, 1, 500000.00, 'Business', 240, 50.0, 'High', 'Reject', NOW()),
(6, 3, 600000.00, 'Investment', 180, 40.0, 'High', 'Reject', NOW());