package com.kamiloses.financemanagerapi.services;

import dto.TransactionRequestDTO;

public interface BudgetService {

     String checkBudgetLimit(Long accountId, TransactionRequestDTO request);

}

