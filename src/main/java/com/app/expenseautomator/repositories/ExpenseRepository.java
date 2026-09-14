package com.app.expenseautomator.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.expenseautomator.entity.Expense;
import com.app.expenseautomator.entity.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    public List<Expense> findByUser(User user);

    public Optional<Expense> findByUserAndId(User user, Long id);
    
}
