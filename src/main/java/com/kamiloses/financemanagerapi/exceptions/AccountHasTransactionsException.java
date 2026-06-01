package com.kamiloses.financemanagerapi.exceptions;

public class AccountHasTransactionsException extends RuntimeException {
    public AccountHasTransactionsException(String message) {
        super(message);
    }
}
