package com.kamiloses.financemanagerapi.services;


import com.kamiloses.financemanagerapi.dto.AccountSummaryResponseDTO;

public interface AccountSummaryService {


    AccountSummaryResponseDTO getSummary(Long accountId);
}
