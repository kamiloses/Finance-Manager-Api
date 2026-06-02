package com.kamiloses.financemanagerapi.repository;

import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.entity.Transaction;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
class TransactionRepositoryTest {
//TODO
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldFindByAccountId() {

        Account account = accountRepository.save(Account.builder()
                .name("A1")
                .balance(BigDecimal.ZERO)
                .build());

        transactionRepository.save(Transaction.builder()
                .account(account)
                .amount(new BigDecimal("100"))
                .type(TransactionType.INCOME)
                .category("Salary")
                .date(LocalDateTime.now())
                .build());

        List<Transaction> result =
                transactionRepository.findByAccountId(account.getId());

        assertEquals(1, result.size());
    }

    @Test
    void shouldFilterByCategory() {

        Account account = accountRepository.save(Account.builder()
                .name("A1")
                .balance(BigDecimal.ZERO)
                .build());

        transactionRepository.save(Transaction.builder()
                .account(account)
                .amount(new BigDecimal("50"))
                .type(TransactionType.EXPENSE)
                .category("Food")
                .date(LocalDateTime.now())
                .build());

        List<Transaction> result =
                transactionRepository.findFiltered(
                        account.getId(),
                        null,
                        null,
                        "Food"
                );

        assertEquals(1, result.size());
    }

    @Test
    void shouldSumExpenses() {

        Account account = accountRepository.save(Account.builder()
                .name("A1")
                .balance(BigDecimal.ZERO)
                .build());

        LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(Transaction.builder()
                .account(account)
                .amount(new BigDecimal("30"))
                .type(TransactionType.EXPENSE)
                .category("Food")
                .date(now)
                .build());

        transactionRepository.save(Transaction.builder()
                .account(account)
                .amount(new BigDecimal("70"))
                .type(TransactionType.EXPENSE)
                .category("Food")
                .date(now)
                .build());

        Optional<BigDecimal> result =
                transactionRepository.sumExpenses(
                        account.getId(),
                        "Food",
                        TransactionType.EXPENSE,
                        now.minusDays(1),
                        now.plusDays(1)
                );

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("100.00"), result.get());
    }
}