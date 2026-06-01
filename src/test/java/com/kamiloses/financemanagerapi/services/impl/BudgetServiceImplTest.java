package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.entity.BudgetLimit;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import com.kamiloses.financemanagerapi.repository.BudgetLimitRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {


    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetLimitRepository budgetLimitRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    void shouldReturnNullWhenBudgetNotExceeded() {

        Long accountId = 1L;

        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setCategory("FOOD");
        request.setAmount(new BigDecimal("100"));

        when(transactionRepository.sumExpenses(
                anyLong(),
                eq("FOOD"),
                eq(TransactionType.EXPENSE),
                any(),
                any()
        )).thenReturn(Optional.of(new BigDecimal("200")));

        BudgetLimit limit = new BudgetLimit();
        limit.setLimitAmount(new BigDecimal("500"));

        when(budgetLimitRepository.findByCategory("FOOD"))
                .thenReturn(Optional.of(limit));

        String result = budgetService.checkBudgetLimit(accountId, request);

        assertNull(result);
    }

    @Test
    void shouldReturnWarningWhenBudgetExceeded() {

        Long accountId = 1L;

        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setCategory("FOOD");
        request.setAmount(new BigDecimal("400"));

        when(transactionRepository.sumExpenses(
                anyLong(),
                eq("FOOD"),
                eq(TransactionType.EXPENSE),
                any(),
                any()
        )).thenReturn(Optional.of(new BigDecimal("700")));

        BudgetLimit limit = new BudgetLimit();
        limit.setLimitAmount(new BigDecimal("1000"));

        when(budgetLimitRepository.findByCategory("FOOD"))
                .thenReturn(Optional.of(limit));

        String result = budgetService.checkBudgetLimit(accountId, request);

        assertEquals("Budget exceeded for category: FOOD", result);
    }


}