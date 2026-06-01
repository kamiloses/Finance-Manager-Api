package com.kamiloses.financemanagerapi.services.impl;


import com.kamiloses.financemanagerapi.dto.AccountRequestDTO;
import com.kamiloses.financemanagerapi.dto.AccountResponseDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.exceptions.AccountAlreadyExistsException;
import com.kamiloses.financemanagerapi.exceptions.AccountHasTransactionsException;
import com.kamiloses.financemanagerapi.exceptions.AccountNotFoundException;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.financemanagerapi.services.AccountService;
import com.kamiloses.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;



    @Override //Celowo paginacji nie dałem
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequest) {

        if (accountRepository.existsByName(accountRequest.getName())) {
            throw new AccountAlreadyExistsException(
                    "Account already exists: " + accountRequest.getName()
            );
        }

        Account account = new Account();
        account.setName(accountRequest.getName());




        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + id)
                );

        return accountMapper.toDto(account);
    }

    @Override
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found: " + id)
                );

        boolean hasAnyTransactions =
                transactionRepository.existsByAccountId(id);

        if (hasAnyTransactions) {
            throw new AccountHasTransactionsException(
                    "Cannot delete account with transactions");
        }

        accountRepository.delete(account);
    }
}