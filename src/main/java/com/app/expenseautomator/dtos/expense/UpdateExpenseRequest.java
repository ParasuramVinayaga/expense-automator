package com.app.expenseautomator.dtos.expense;

import java.time.LocalDate;

import com.app.expenseautomator.enums.ExpenseFrequency;
import com.app.expenseautomator.validations.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateExpenseRequest {
    
    @Size(min = 3, max = 30, message = "Name should be between 3 to 30 characters")
    private String name;

    @Min(value = 1, message = "Expense should neither be and nor less than 0")
    private Float value;

    @ValidEnum(enumClass = ExpenseFrequency.class)
    private String expenseFrequency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setExpenseFrequency(String frequency) {
        expenseFrequency = frequency;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getExpenseFrequency() {
        return expenseFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Float getValue() {
        return value;
    }
}
