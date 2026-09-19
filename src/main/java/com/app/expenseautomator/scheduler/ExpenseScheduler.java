package com.app.expenseautomator.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.expenseautomator.entity.User;
import com.app.expenseautomator.services.ExpenseService;
import com.app.expenseautomator.services.UserService;

@Component 
public class ExpenseScheduler {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Scheduled(fixedRate = 60000)
    public void syncExpenses() {
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            expenseService.syncExpenseFor(user);
        }
    }
}
