package com.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankStatementProcessor {
    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public double calculateTotalAmount() {
        return bankTransactions.stream().map(BankTransaction::getAmount).reduce(Double::sum).orElse(0d);
    }

    public double calculateTotalInMonth(LocalDate month) {
        final DateTimeFormatter formatMonth = DateTimeFormatter.ofPattern("MM-yyyy");
        return bankTransactions.stream()
                .filter(bankTransaction -> month.format(formatMonth).equals(bankTransaction.getDate().format(formatMonth)))
                .map(BankTransaction::getAmount)
                .reduce(Double::sum).orElse(0d);
    }

    public double calculateTotalForCategory(String category) {
        return bankTransactions.stream()
                .filter(bankTransaction -> category.equals(bankTransaction.getDescription()))
                .map(BankTransaction::getAmount)
                .reduce(Double::sum).orElse(0d);
    }
}
