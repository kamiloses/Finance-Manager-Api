package com.kamiloses.financemanagerapi.mapper;

import com.kamiloses.financemanagerapi.dto.AccountResponseDTO;
import com.kamiloses.financemanagerapi.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public AccountResponseDTO toDto(Account account) {

        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setBalance(account.getBalance());

        return dto;
    }
}