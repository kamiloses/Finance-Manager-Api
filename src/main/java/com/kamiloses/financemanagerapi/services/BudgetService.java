package com.kamiloses.financemanagerapi.services;


public interface BudgetService {

     String checkBudgetLimit(Long accountId, TransactionRequestDTO request);

}

