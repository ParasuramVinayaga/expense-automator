package com.app.expenseautomator.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ExpenseHistory {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY) 
    @JoinColumn(name = "expense_id", referencedColumnName = "id")
    private List<Expense> expense;

    @Column(nullable = false)
    private LocalDate expenseLoggedOn;


    public Long getId() {
        return id;
    }

    public List<Expense> getExpense() {
        return expense;
    }

    public LocalDate getExpenseLoggedOn() {
        return expenseLoggedOn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }

    public void setExpenseLoggedOn(LocalDate date) {
        expenseLoggedOn = date;
    }
}