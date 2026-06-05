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

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.date DESC")
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);


    @Query("""
                SELECT SUM(t.amount) FROM Transaction t
                WHERE t.account.id = :accountId
                  AND t.category = :category AND t.type = :type
                  AND t.date BETWEEN :from AND :to
            """)
    Optional<BigDecimal> sumExpenses(
            @Param("accountId") Long accountId, @Param("category") String category, @Param("type") TransactionType type,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);


    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM Transaction t
                WHERE t.account.id = :accountId
                  AND t.type = :type
            """)
    BigDecimal sumByType(@Param("accountId") Long accountId, @Param("type") TransactionType type);


    @Query("""
                SELECT t.category, SUM(t.amount)
                FROM Transaction t
                WHERE t.account.id = :accountId
                  AND t.type = 'EXPENSE'
                GROUP BY t.category
            """)
    List<Object[]> sumByCategory(@Param("accountId") Long accountId);
}