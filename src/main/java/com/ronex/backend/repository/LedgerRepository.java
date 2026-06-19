package com.ronex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ronex.backend.model.Ledger;
import java.util.List;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    List<Ledger> findByUserId(Long userId);
    
    List<Ledger> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT FUNCTION('date', l.createdAt), SUM(l.amount) " +
    	       "FROM Ledger l WHERE l.type = 'CREDIT' " +
    	       "GROUP BY FUNCTION('date', l.createdAt) " +
    	       "ORDER BY FUNCTION('date', l.createdAt)")
    	List<Object[]> getDailyRevenue();
}