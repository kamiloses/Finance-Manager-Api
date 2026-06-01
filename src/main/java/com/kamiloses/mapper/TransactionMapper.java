package com.kamiloses.mapper;

import com.kamiloses.financemanagerapi.dto.TransactionResponseDTO;
import com.kamiloses.financemanagerapi.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toDto(Transaction t) {

        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setType(t.getType());
        dto.setCategory(t.getCategory());
        dto.setDescription(t.getDescription());
        dto.setDate(t.getDate());
        dto.setAccountId(t.getAccount().getId());

        return dto;
    }

}