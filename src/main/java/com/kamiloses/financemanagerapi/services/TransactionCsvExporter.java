package com.kamiloses.financemanagerapi.services;

public interface TransactionCsvExporter {


    public String exportTransactionsToCsv(Long accountId);

}
