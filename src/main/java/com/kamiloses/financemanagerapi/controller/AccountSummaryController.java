package com.kamiloses.financemanagerapi.controller;

import com.kamiloses.financemanagerapi.dto.AccountSummaryResponseDTO;
import com.kamiloses.financemanagerapi.services.AccountSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/{accountId}/summary")
@RequiredArgsConstructor
public class AccountSummaryController {

    private final AccountSummaryService accountSummaryService;

    @GetMapping
    public ResponseEntity<AccountSummaryResponseDTO> getSummary(
            @PathVariable Long accountId
    ) {
        return ResponseEntity.ok(
                accountSummaryService.getSummary(accountId)
        );
    }
}