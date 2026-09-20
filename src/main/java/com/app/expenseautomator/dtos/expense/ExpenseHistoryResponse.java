package com.app.expenseautomator.dtos.expense;

import java.time.LocalDate;

import com.app.expenseautomator.entity.ExpenseHistory;

public class ExpenseHistoryResponse {
    
    private ExpenseHistory history;

    public ExpenseHistoryResponse(ExpenseHistory history) {
        this.history = history;
    }

    public Long getExpenseHistoryId() {
        return history.getId();
    }

    public LocalDate getExpenseLogDate() {
        return history.getExpenseLoggedOn();
    }
}
