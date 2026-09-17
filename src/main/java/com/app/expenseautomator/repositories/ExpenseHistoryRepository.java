package com.app.expenseautomator.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.expenseautomator.entity.ExpenseHistory;

@Repository 
public interface ExpenseHistoryRepository extends JpaRepository<ExpenseHistory, Long> {

    @Query("""
        SELECT h
        FROM ExpenseHistory h 
        WHERE h.expense.id = :expenseId
        ORDER BY h.expenseLoggedOn DESC
        """)
    public List<ExpenseHistory> getLatestHistoryForExpense(@Param("expenseId") Long expenseId, Pageable pageable);
    
}
