package com.app.expenseautomator.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.app.expenseautomator.dtos.expense.CreateExpenseRequest;
import com.app.expenseautomator.dtos.expense.UpdateExpenseRequest;
import com.app.expenseautomator.entity.Expense;
import com.app.expenseautomator.entity.ExpenseHistory;
import com.app.expenseautomator.entity.User;
import com.app.expenseautomator.enums.ExpenseFrequency;
import com.app.expenseautomator.exceptions.InvalidExpenseException;
import com.app.expenseautomator.repositories.ExpenseHistoryRepository;
import com.app.expenseautomator.repositories.ExpenseRepository;

import io.micrometer.common.util.StringUtils;

@Service
public class ExpenseService {
    
    private ExpenseRepository expenseRepo;
    private ExpenseHistoryRepository expenseHistoryRepo;
    private UserService userService;

    private final HashMap<ExpenseFrequency, Function<LocalDate, LocalDate>> calMap = new HashMap<>();

    public ExpenseService(
        ExpenseRepository expenseRepo, 
        ExpenseHistoryRepository expenseHistoryRepo, 
        UserService userService
    ) {
        this.expenseRepo = expenseRepo;
        this.expenseHistoryRepo = expenseHistoryRepo;
        this.userService = userService;
        setFrequencyMap();
    }

    private void setFrequencyMap() {

        Function<LocalDate, LocalDate> nextDayFunction = (d) -> d
            .plusDays(1)
            .atStartOfDay()
            .toLocalDate();
        
        Function<LocalDate, LocalDate> nextWeekFunction = (d) -> d
            .plusWeeks(1)
            .atStartOfDay()
            .toLocalDate();

        Function<LocalDate, LocalDate> nextMonthFunction = (d) -> d
            .plusMonths(1)
            .atStartOfDay()
            .toLocalDate();

        Function<LocalDate, LocalDate> nextYearFunction = (d) -> d
            .plusYears(1)
            .atStartOfDay()
            .toLocalDate();

        calMap.put(ExpenseFrequency.DAILY, nextDayFunction);
        calMap.put(ExpenseFrequency.WEEKLY, nextWeekFunction);
        calMap.put(ExpenseFrequency.MONTHLY, nextMonthFunction);
        calMap.put(ExpenseFrequency.YEARLY, nextYearFunction);
    }

    public User getAuthUser() {
        return userService.getAuthenticatedUser();
    }

    public Expense createExpense(CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setName(request.getName());
        LocalDate startDate = request.getStartDate();
        expense.setStartDate(startDate);

        LocalDate endDate = request.getEndDate();
        if (!ObjectUtils.isEmpty(endDate)) {
            expense.setEndDate(endDate);
        }

        expense.setUser(getAuthUser());
        expense.setFrequency(ExpenseFrequency.valueOf(request.getExpenseFrequency()));
        expense.setValue(request.getValue());
        return expenseRepo.save(expense);
    }

    public List<Expense> listByUser(User user) {
        return expenseRepo.findByUser(user);
    }

    public List<Expense> listAuthUserExpenses() {
        return listByUser(getAuthUser());
    }

    public Expense updateExpense(Long id, UpdateExpenseRequest request) {
        Optional<Expense> optionalExpense = expenseRepo.findByUserAndId(getAuthUser(), id);

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

        String frequency = request.getExpenseFrequency();
        if (!StringUtils.isBlank(frequency)) {
            expenseToUpdate.setFrequency(
                ExpenseFrequency.valueOf(frequency.toUpperCase().trim())
            );
        }

        LocalDate endDate = request.getEndDate();
        if (endDate != null) {
            expenseToUpdate.setEndDate(endDate);
        }

        if (endDate == null) {
            endDate = expenseToUpdate.getEndDate();
        }

        LocalDate startDate = request.getStartDate();
        if (startDate != null && startDate.isBefore(endDate)) {
            expenseToUpdate.setStartDate(startDate);
        }

        return expenseRepo.save(expenseToUpdate);
    }

    // Method to sync expenses
    public void syncExpenseFor(User user) {
        List<Expense> expenses = listByUser(user);

        for(Expense expense : expenses) {

            List<ExpenseHistory> optionalHistory = expenseHistoryRepo.getLatestHistoryForExpense(
                expense.getId(), 
                PageRequest.of(0, 1)
            );

            if (optionalHistory.isEmpty()) {
                ExpenseHistory history = new ExpenseHistory();
                history.setExpense(expense);
                history.setExpenseLoggedOn(expense.getStartDate());
                expenseHistoryRepo.save(history);
                continue;
            }

            ExpenseHistory history = optionalHistory.get(0);
            LocalDate nextLogDate = getNexPeriodByFrequency(
                expense.getFrequency(), 
                history.getExpenseLoggedOn()
            );

            LocalDate expenseEndDate = expense.getEndDate();
            if (expenseEndDate != null && expenseEndDate.isBefore(nextLogDate)) {
                continue;
            }

            if (expenseEndDate == null && nextLogDate.isAfter(LocalDate.now())) {
                continue;
            }

            expenseHistoryRepo.save(new ExpenseHistory(history.getExpense(), nextLogDate));
        }
    }

    public LocalDate getNexPeriodByFrequency(ExpenseFrequency frequency, LocalDate date) {
        return calMap.get(frequency).apply(date);
    }
}
