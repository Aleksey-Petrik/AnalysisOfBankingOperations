package com.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<BankTransaction> findMaxBankTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        return findBankTransactions(startDate, endDate, (maxAmount, bankAmount) -> maxAmount < bankAmount, Double.MIN_VALUE);
    }

    public List<BankTransaction> findMinBankTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        return findBankTransactions(startDate, endDate, (minAmount, bankAmount) -> minAmount > bankAmount, Double.MAX_VALUE);
    }

    public List<BankTransaction> findBankTransactions(LocalDate startDate, LocalDate endDate, BiPredicate<Double, Double> filterAmount, double valueFilter) {
        List<BankTransaction> listMaxBankTransactions = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (startDate.compareTo(bankTransaction.getDate()) <= 0
                    && endDate.compareTo(bankTransaction.getDate()) >= 0) {
                if (filterAmount.test(valueFilter, bankTransaction.getAmount())) {
                    valueFilter = bankTransaction.getAmount();
                    listMaxBankTransactions.clear();
                }
                if (valueFilter == bankTransaction.getAmount()) {
                    listMaxBankTransactions.add(bankTransaction);
                }
            }
        }
        return listMaxBankTransactions;
    }

    public List<BankTransaction> findTransactions(BankTransactionFilter bankTransactionFilter) {
        return bankTransactions.stream()
                .filter(bankTransactionFilter::test)
                .collect(Collectors.toList());
    }

    public Map<String, Double> groupingMonths() {
        return groupingAllBanksTransactions(bankTransaction -> bankTransaction.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")));
    }

    public Map<String, Double> groupingCategories() {
        return groupingAllBanksTransactions(BankTransaction::getDescription);
    }

    public Map<String, Map<String, Double>> groupingMonthsForCategories() {
        return bankTransactions.stream()
                .collect(Collectors.groupingBy(bankTransaction -> bankTransaction.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")),
                        Collectors.groupingBy(BankTransaction::getDescription, Collectors.summingDouble(BankTransaction::getAmount))));
    }

    private Map<String, Double> groupingAllBanksTransactions(Function<BankTransaction, String> grouping) {
        return bankTransactions.stream().collect(Collectors.groupingBy(grouping, Collectors.summingDouble(BankTransaction::getAmount)));
    }
}
