package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.entity.BudgetLimit;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import com.kamiloses.financemanagerapi.repository.BudgetLimitRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.financemanagerapi.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

private final TransactionRepository transactionRepository;
private final BudgetLimitRepository budgetLimitRepository;







    @Override
    public String checkBudgetLimit(Long accountId, TransactionRequestDTO request) {

        LocalDateTime start = getMonthStart();
        LocalDateTime end = getNow();

        BigDecimal spent = transactionRepository
                .sumExpenses(
                        accountId,
                        request.getCategory(),
                        TransactionType.EXPENSE,
                        start,
                        end
                )
                .orElse(BigDecimal.ZERO);

        BudgetLimit limit = budgetLimitRepository
                .findByCategory(request.getCategory())
                .orElse(null);

        if (limit == null) {
            return null;
        }

        BigDecimal newTotal = spent.add(request.getAmount());

        if (newTotal.compareTo(limit.getLimitAmount()) > 0) {
            return "Budget exceeded for category: " + request.getCategory();
        }

        return null;
    }
    private LocalDateTime getMonthStart() {
        return YearMonth.now()
                .atDay(1)
                .atStartOfDay();
    }
    private LocalDateTime getNow() {
        return LocalDateTime.now();
    }


}
