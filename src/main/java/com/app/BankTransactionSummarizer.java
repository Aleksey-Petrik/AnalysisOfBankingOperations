package com.app;

import com.app.model.BankTransaction;

public interface BankTransactionSummarizer {
    double summarize(double accumulator, BankTransaction bankTransaction);
}