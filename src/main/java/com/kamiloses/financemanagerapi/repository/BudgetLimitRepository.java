package com.kamiloses.financemanagerapi.repository;

import com.kamiloses.financemanagerapi.entity.BudgetLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetLimitRepository extends JpaRepository<BudgetLimit, Long> {



    Optional<BudgetLimit> findByCategory(String category);
}
