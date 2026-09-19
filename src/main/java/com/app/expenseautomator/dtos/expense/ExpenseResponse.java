package com.app.expenseautomator.dtos.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.app.expenseautomator.entity.Expense;
import com.app.expenseautomator.enums.ExpenseFrequency;

public class ExpenseResponse {
    
    private Long id;
    private String name;
    private ExpenseFrequency frequency;
    private Float value;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public ExpenseResponse(Expense expense) {
        id = expense.getId();
        name = expense.getName();
        frequency = expense.getFrequency();
        value = expense.getValue();
        startDate = expense.getStartDate();
        endDate = expense.getEndDate();
        createdAt = expense.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ExpenseFrequency getExpenseType() {
        return frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Float getValue() {
        return value;
    }
}
