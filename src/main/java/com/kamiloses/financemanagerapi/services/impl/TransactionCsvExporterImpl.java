package com.kamiloses.financemanagerapi.services.impl;

import com.kamiloses.financemanagerapi.entity.Transaction;
import com.kamiloses.financemanagerapi.repository.TransactionRepository;
import com.kamiloses.financemanagerapi.services.TransactionCsvExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionCsvExporterImpl implements TransactionCsvExporter {

    private final TransactionRepository transactionRepository;




    public String exportTransactionsToCsv(Long accountId) {

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        StringBuilder csv = new StringBuilder();


        csv.append("date,amount,type,category,description\n");

        for (Transaction t : transactions) {

            csv.append(t.getDate()).append(",")
                    .append(t.getAmount()).append(",")
                    .append(t.getType()).append(",")
                    .append(t.getCategory()).append(",")
                    .append(t.getDescription() != null ? t.getDescription() : "")
                    .append("\n");
        }

        return csv.toString();
    }
}
