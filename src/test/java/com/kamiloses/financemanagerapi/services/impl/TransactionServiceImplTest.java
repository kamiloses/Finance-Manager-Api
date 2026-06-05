package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.dto.TransactionResponseDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.entity.Transaction;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import com.kamiloses.financemanagerapi.exceptions.AccountNotFoundException;
import com.kamiloses.financemanagerapi.exceptions.TransactionNotFoundException;
import com.kamiloses.financemanagerapi.mapper.TransactionMapper;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.financemanagerapi.services.BudgetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldCreateIncomeTransactionAndIncreaseBalance() {

        Long accountId = 1L;

        Account account = Account.builder()
                .id(accountId)
                .balance(new BigDecimal("100.00"))
                .build();

        TransactionRequestDTO transactionRequest = new TransactionRequestDTO(
                new BigDecimal("50.00"),
                TransactionType.INCOME,
                "Salary",
                "Test"
        );

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(transactionRequest.getAmount())
                .type(transactionRequest.getType())
                .category(transactionRequest.getCategory())
                .description(transactionRequest.getDescription())
                .date(LocalDateTime.now())
                .build();

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any())).thenReturn(transaction);
        when(transactionMapper.toDto(any())).thenReturn(responseDTO);

        TransactionResponseDTO result =
                transactionService.createTransaction(accountId, transactionRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("150.00"), account.getBalance());
        assertNull(result.getWarning());

        verify(budgetService, never()).checkBudgetLimit(anyLong(), any());
    }

    @Test
    void shouldCreateExpenseTransactionAndDecreaseBalanceWithWarning() {

        Long accountId = 1L;

        Account account = Account.builder()
                .id(accountId)
                .balance(new BigDecimal("100.00"))
                .build();

        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("30.00"),
                TransactionType.EXPENSE,
                "Food",
                "Lunch"
        );

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .description(request.getDescription())
                .date(LocalDateTime.now())
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any())).thenReturn(transaction);
        when(transactionMapper.toDto(any())).thenReturn(new TransactionResponseDTO());
        when(budgetService.checkBudgetLimit(anyLong(), any())).thenReturn("Budget exceeded");

        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(accountId, request);

        assertEquals(new BigDecimal("70.00"), account.getBalance());
        assertNotNull(transactionResponseDTO.getWarning());

    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        TransactionRequestDTO request = new TransactionRequestDTO(
                new BigDecimal("10.00"),
                TransactionType.EXPENSE,
                "Food",
                "Test"
        );

        assertThrows(AccountNotFoundException.class,
                () -> transactionService.createTransaction(1L, request));
    }

    @Test
    void shouldDeleteIncomeTransactionAndDecreaseBalance() {

        Account account = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .build();

        Transaction transaction = Transaction.builder()
                .id(10L)
                .account(account)
                .amount(new BigDecimal("20.00"))
                .type(TransactionType.INCOME)
                .build();

        when(transactionRepository.findById(10L))
                .thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(10L);

        assertEquals(new BigDecimal("80.00"), account.getBalance());

        verify(transactionRepository).delete(transaction);
    }

    @Test
    void shouldDeleteExpenseTransactionAndIncreaseBalance() {

        Account account = Account.builder()
                .id(1L)
                .balance(new BigDecimal("100.00"))
                .build();

        Transaction transaction = Transaction.builder()
                .id(10L)
                .account(account)
                .amount(new BigDecimal("30.00"))
                .type(TransactionType.EXPENSE)
                .build();

        when(transactionRepository.findById(10L))
                .thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(10L);

        assertEquals(new BigDecimal("130.00"), account.getBalance());
    }

}