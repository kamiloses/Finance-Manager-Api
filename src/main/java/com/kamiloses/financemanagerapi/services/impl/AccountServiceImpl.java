package com.kamiloses.financemanagerapi.services.impl;

import dto.AccountRequestDTO;
import dto.AccountResponseDTO;
import entities.Account;
import ex.AccountAlreadyExistsException;
import ex.AccountHasTransactionsException;
import ex.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import mapper.AccountMapper;
import org.springframework.stereotype.Service;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import services.AccountService;

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