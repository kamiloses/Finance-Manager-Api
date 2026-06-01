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
import com.kamiloses.financemanagerapi.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BudgetService budgetService;

    @Override
    public List<TransactionResponseDTO> getTransactions(Long accountId, LocalDateTime from, LocalDateTime to, String category)
     {

        return transactionRepository.findFiltered(accountId,from,to,category)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(Long accountId, TransactionRequestDTO request) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + accountId)
                );

        Transaction transaction = buildTransaction(account, request);

        String warning = applyTransaction(account, request);

        transactionRepository.save(transaction);

        TransactionResponseDTO response = transactionMapper.toDto(transaction);
        response.setWarning(warning);

        return response;
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transaction not found: " + transactionId)
                );

        Account account = transaction.getAccount();


        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));

        }

        transactionRepository.delete(transaction);
    }




//
//
//    public String exportTransactionsToCsv(Long accountId) {
//
//        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
//
//        StringBuilder csv = new StringBuilder();
//
//
//        csv.append("date,amount,type,category,description\n");
//
//        for (Transaction t : transactions) {
//
//            csv.append(t.getDate()).append(",")
//                    .append(t.getAmount()).append(",")
//                    .append(t.getType()).append(",")
//                    .append(t.getCategory()).append(",")
//                    .append(t.getDescription() != null ? t.getDescription() : "")
//                    .append("\n");
//        }
//
//        return csv.toString();
//    }

    private Transaction buildTransaction(Account account, TransactionRequestDTO request) {
        return Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .description(request.getDescription())
                .date(LocalDateTime.now())
                .build();
    }

    private String applyTransaction(Account account, TransactionRequestDTO request) {

        if (request.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(request.getAmount()));
            return null;
        }

        String warning = budgetService.checkBudgetLimit(account.getId(), request);

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        return warning;
    }
}
