package com.kamiloses.financemanagerapi.services;


import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;

public interface BudgetService {

     String checkBudgetLimit(Long accountId, TransactionRequestDTO request);

}

