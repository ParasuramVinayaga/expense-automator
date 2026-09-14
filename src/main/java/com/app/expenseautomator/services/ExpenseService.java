package com.app.expenseautomator.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.expenseautomator.dtos.expense.CreateExpenseRequest;
import com.app.expenseautomator.dtos.expense.UpdateExpenseRequest;
import com.app.expenseautomator.entity.Expense;
import com.app.expenseautomator.entity.User;
import com.app.expenseautomator.enums.ExpenseType;
import com.app.expenseautomator.exceptions.InvalidExpenseException;
import com.app.expenseautomator.repositories.ExpenseRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class ExpenseService {
    
    private ExpenseRepository repository;
    private UserService userService;

    public ExpenseService(ExpenseRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public User getAuthUser() {
        return userService.getAuthenticatedUser();
    }

    public Expense createExpense(CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setName(request.getName());
        LocalDateTime startTime = request.getStartTime().atStartOfDay();
        expense.setStartTime(startTime);

        LocalDate endTime = request.getEndTime();
        if (!ObjectUtils.isEmpty(endTime)) {
            expense.setEndTime(endTime.atTime(LocalTime.MAX));
        }

        expense.setUser(getAuthUser());
        expense.setExpenseType(ExpenseType.valueOf(request.getExpenseType()));
        expense.setValue(request.getValue());
        return repository.save(expense);
    }

    public List<Expense> listAuthUserExpenses() {
        return repository.findByUser(getAuthUser());
    }

    public Expense updateExpense(Long id, UpdateExpenseRequest request) {
        Optional<Expense> optionalExpense = repository.findByUserAndId(getAuthUser(), id);

        if (optionalExpense.isEmpty()) {
            throw new InvalidExpenseException();
        }

        Expense expenseToUpdate = optionalExpense.get();

        String expenseName = request.getName();
        if (!StringUtils.isBlank(expenseName)) {
            expenseToUpdate.setName(expenseName);
        }

        Float expenseValue = request.getValue();
        if (expenseValue != null) {
            expenseToUpdate.setName(expenseName);
        }

        String expenseTypeString = request.getExpenseType();
        if (!StringUtils.isBlank(expenseTypeString)) {
            ExpenseType expenseType = ExpenseType.valueOf(expenseTypeString.toUpperCase().trim());
            expenseToUpdate.setExpenseType(expenseType);
        }

        LocalDate endTime = request.getEndTime();
        if (endTime != null) {
            expenseToUpdate.setEndTime(endTime.atTime(LocalTime.MAX));
        }

        if (endTime == null) {
            endTime = expenseToUpdate.getEndTime().toLocalDate();
        }

        LocalDate startTime = request.getStartTime();
        if (startTime != null && startTime.isBefore(endTime)) {
            expenseToUpdate.setStartTime(startTime.atStartOfDay());
        }

        return repository.save(expenseToUpdate);
    }
}
