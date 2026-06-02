package com.kamiloses.financemanagerapi.controller;

import com.kamiloses.financemanagerapi.dto.TransactionRequestDTO;
import com.kamiloses.financemanagerapi.dto.TransactionResponseDTO;
import com.kamiloses.financemanagerapi.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(@PathVariable Long accountId, @RequestParam(required = false) LocalDateTime from,
                                                                        @RequestParam(required = false) LocalDateTime to, @RequestParam(required = false) String category) {

        return ResponseEntity.ok(
                transactionService.getTransactions(accountId, from, to, category));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@PathVariable Long accountId,@Valid @RequestBody TransactionRequestDTO request) {

        TransactionResponseDTO created = transactionService.createTransaction(accountId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long transactionId
    ) {

        transactionService.deleteTransaction(transactionId);

        return ResponseEntity.noContent().build();
    }
}