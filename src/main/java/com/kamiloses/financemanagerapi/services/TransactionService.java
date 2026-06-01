package com.kamiloses.financemanagerapi.services;


import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.dto.TransactionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    List<TransactionResponseDTO> getTransactions(Long accountId, LocalDateTime from, LocalDateTime to, String category);
    TransactionResponseDTO createTransaction(Long accountId, TransactionRequestDTO request);

    void deleteTransaction(Long transactionId);


}