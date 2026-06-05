package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.dto.AccountSummaryResponseDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.entity.Transaction;
import com.kamiloses.financemanagerapi.entity.TransactionType;
import com.kamiloses.financemanagerapi.exceptions.AccountNotFoundException;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.financemanagerapi.services.AccountSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountSummaryServiceImpl implements AccountSummaryService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public AccountSummaryResponseDTO getSummary(Long accountId) {

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        BigDecimal income =
                transactionRepository.sumByType(accountId, TransactionType.INCOME);

        BigDecimal expense =
                transactionRepository.sumByType(accountId, TransactionType.EXPENSE);

        Map<String, BigDecimal> byCategory = transactionRepository
                .sumByCategory(accountId)
                .stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> (BigDecimal) r[1]
                ));

        return new AccountSummaryResponseDTO(income, expense, byCategory);
    }
}