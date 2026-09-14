package com.app.expenseautomator.dtos.expense;

import java.time.LocalDate;

import com.app.expenseautomator.enums.ExpenseType;
import com.app.expenseautomator.validations.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateExpenseRequest {
    
    @Size(min = 3, max = 30, message = "Name should be between 3 to 30 characters")
    private String name;

    @Min(value = 1, message = "Expense should neither be and nor less than 0")
    private Float value;

    @ValidEnum(enumClass = ExpenseType.class)
    private String expenseType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    public void setName(String name) {
        this.name = name;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public Float getValue() {
        return value;
    }
}
