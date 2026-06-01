package com.kamiloses.financemanagerapi.repository;

import com.kamiloses.financemanagerapi.entity.Transaction;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    boolean existsByAccountId(Long id);


    List<Transaction> findByAccountId(Long accountId);


    @Query("""
                SELECT t FROM Transaction t
                WHERE t.account.id = :accountId
                AND (:from IS NULL OR t.date >= :from)
                AND (:to IS NULL OR t.date <= :to)
                AND (:category IS NULL OR LOWER(t.category) = LOWER(:category))
                ORDER BY t.date DESC
            """)
    List<Transaction> findFiltered(
            @Param("accountId") Long accountId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("category") String category
    );


    @Query("""
                SELECT SUM(t.amount)
                FROM Transaction t
                WHERE t.account.id = :accountId
                  AND t.category = :category
                  AND t.type = :type
                  AND t.date BETWEEN :from AND :to
            """)
    Optional<BigDecimal> sumExpenses(
            Long accountId,
            String category,
            TransactionType type,
            LocalDateTime from,
            LocalDateTime to
    );

}




