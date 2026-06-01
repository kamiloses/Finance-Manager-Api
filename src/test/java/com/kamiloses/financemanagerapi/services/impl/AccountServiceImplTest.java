package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.dto.AccountRequestDTO;
import com.kamiloses.financemanagerapi.dto.AccountResponseDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import com.kamiloses.financemanagerapi.exceptions.AccountAlreadyExistsException;
import com.kamiloses.financemanagerapi.exceptions.AccountHasTransactionsException;
import com.kamiloses.financemanagerapi.exceptions.AccountNotFoundException;
import com.kamiloses.financemanagerapi.repository.AccountRepository;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {




    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldCreateAccountSuccessfully() {

        when(accountRepository.existsByName("Main account")).thenReturn(false);

        Account account = new Account();
        account.setId(1L);
        account.setName("Main account");

        AccountResponseDTO response = new AccountResponseDTO(1L, "Main account", null);

        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(response);

        AccountRequestDTO request = new AccountRequestDTO();
        request.setName("Main account");

        AccountResponseDTO result = accountService.createAccount(request);

        assertEquals("Main account", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenAccountAlreadyExists() {

        when(accountRepository.existsByName("Main account")).thenReturn(true);

        AccountRequestDTO request = new AccountRequestDTO();
        request.setName("Main account");

        assertThrows(AccountAlreadyExistsException.class,
                () -> accountService.createAccount(request));
    }

    @Test
    void shouldReturnAccountById() {

        Account account = new Account();
        account.setId(1L);
        account.setName("Main");

        AccountResponseDTO dto = new AccountResponseDTO(1L, "Main", null);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(dto);

        AccountResponseDTO result = accountService.getAccountById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountById(1L));
    }

    @Test
    void shouldThrowExceptionWhenAccountHasTransactions() {

        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByAccountId(1L)).thenReturn(true);

        assertThrows(AccountHasTransactionsException.class,
                () -> accountService.deleteAccount(1L));
    }

    @Test
    void shouldDeleteAccountSuccessfully() {

        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByAccountId(1L)).thenReturn(false);

        accountService.deleteAccount(1L);

        verify(accountRepository).delete(account);
    }
}


