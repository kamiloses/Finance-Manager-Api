package com.kamiloses.financemanagerapi.services;

import dto.AccountRequestDTO;
import dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    List<AccountResponseDTO> getAllAccounts();

    AccountResponseDTO createAccount(AccountRequestDTO request);

    AccountResponseDTO getAccountById(Long id);

    void deleteAccount(Long id);
}