package com.kamiloses.financemanagerapi.controller;

import com.kamiloses.financemanagerapi.services.TransactionCsvExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionCsvController {

    private final TransactionCsvExporter transactionCsvExporter;

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv(@PathVariable Long accountId) {
        String csv = transactionCsvExporter.exportTransactionsToCsv(accountId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

}