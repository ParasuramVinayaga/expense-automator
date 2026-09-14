package com.app.expenseautomator.exceptions;

public class InvalidExpenseException extends RuntimeException {
    
    public InvalidExpenseException () {
        super("Seleted Expense Is Invalid");
    }
}
