package com.app.expenseautomator.dtos.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.app.expenseautomator.entity.Expense;
import com.app.expenseautomator.entity.ExpenseHistory;
import com.app.expenseautomator.enums.ExpenseFrequency;

public class ExpenseResponse {
    
    private Expense expense;

    public ExpenseResponse(Expense expense) {
        this.expense = expense;
    }

    public Long getId() {
        return expense.getId();
    }

    public String getName() {
        return expense.getName();
    }

    public ExpenseFrequency getExpenseType() {
        return expense.getFrequency();
    }

    public LocalDate getStartDate() {
        return expense.getStartDate();
    }

    public LocalDate getEndDate() {
        return expense.getEndDate();
    }

    public LocalDateTime getCreatedAt() {
        return expense.getCreatedAt();
    }

    public Float getValue() {
        return expense.getValue();
    }

    public List<ExpenseHistoryResponse> getHistories() {
        return expense.getHistories().stream()
        .sorted(Comparator.comparing(ExpenseHistory::getExpenseLoggedOn))
        .map((history) -> new ExpenseHistoryResponse(history))
        .toList();
    }
}
