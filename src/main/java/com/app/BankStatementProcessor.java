package com.app;

import com.app.model.BankTransaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BankStatementProcessor {
    private static final DateTimeFormatter FORMAT_MONTH = DateTimeFormatter.ofPattern("MM.yyyy");

    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    /*
     * Создаем объект с общей информацией
     * */
    public SummaryStatistics getSummaryStatistics() {
        DoubleSummaryStatistics doubleSummaryStatistics = bankTransactions.stream()
                .mapToDouble(BankTransaction::getAmount).summaryStatistics();

        return new SummaryStatistics.Builder()
                .sum(doubleSummaryStatistics.getSum())
                .max(doubleSummaryStatistics.getMax())
                .min(doubleSummaryStatistics.getMin())
                .average(doubleSummaryStatistics.getAverage())
                .build();
    }

    /*
     * Суммирование всех транзакций
     * */
    public double calculateTotalAmount() {
        return bankTransactions.stream()
                .map(BankTransaction::getAmount)
                .reduce(Double::sum)
                .orElse(0d);
    }

    /*
     * Суммирование всех транзакций в заданном месяце
     * */
    public double calculateTotalInMonth(LocalDate month) {
        return bankTransactions.stream()
                .filter(bankTransaction -> month.format(FORMAT_MONTH).equals(bankTransaction.getDate().format(FORMAT_MONTH)))
                .map(BankTransaction::getAmount)
                .reduce(Double::sum).orElse(0d);
    }

    /*
     * Суммирование всех транзакций по заданной категории
     * */
    public double calculateTotalForCategory(String category) {
        return bankTransactions.stream()
                .filter(bankTransaction -> category.equals(bankTransaction.getDescription()))
                .map(BankTransaction::getAmount)
                .reduce(Double::sum).orElse(0d);
    }

    /*
     * Суммирование сумм больше либо равной шаблонной
     * */
    public double summationWithAmount(double amount) {
        return summarizeTransactions((summa, bankTransaction) -> bankTransaction.getAmount() >= amount ? bankTransaction.getAmount() : 0);
    }

    public double summarizeTransactions(BankTransactionSummarizer bankTransactionSummarizer) {
        double result = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            result += bankTransactionSummarizer.summarize(result, bankTransaction);
        }
        return result;
    }

    /*
     * Перечень транзакций с наименьшей суммой в заданном периоде
     * */
    public List<BankTransaction> findMaxBankTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        return findBankTransactions(startDate, endDate, (maxAmount, bankAmount) -> maxAmount < bankAmount, Double.MIN_VALUE);
    }

    /*
     * Перечень транзакций с наибольшей суммой в заданном периоде
     * */
    public List<BankTransaction> findMinBankTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        return findBankTransactions(startDate, endDate, (minAmount, bankAmount) -> minAmount > bankAmount, Double.MAX_VALUE);
    }

    /*
     * Общий метод для поиска наименьшей/наибольшей суммы в заданном периоде
     * */
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

    /*
     * Поиск транзакций по условию
     * */
    public List<BankTransaction> findTransactions(BankTransactionFilter bankTransactionFilter) {
        return bankTransactions.stream()
                .filter(bankTransactionFilter::test)
                .collect(Collectors.toList());
    }

    /*
     * Группировка транзакций по месяцу
     * */
    public Map<String, Double> groupingMonths() {
        return groupingAllBanksTransactions(bankTransaction -> bankTransaction.getDate().format(FORMAT_MONTH));
    }

    /*
     * Группировка транзакций по категории
     * */
    public Map<String, Double> groupingCategories() {
        return groupingAllBanksTransactions(BankTransaction::getDescription);
    }

    /*
     * Группировка транзакций по месяцу и по категории
     * */
    public Map<String, Map<String, Double>> groupingMonthsForCategories() {
        return bankTransactions.stream()
                .collect(Collectors.groupingBy(bankTransaction -> bankTransaction.getDate().format(FORMAT_MONTH),
                        Collectors.groupingBy(BankTransaction::getDescription, Collectors.summingDouble(BankTransaction::getAmount))));
    }

    /*
     * Общий метод для группировки транзакций по месяцу или категории
     * */
    private Map<String, Double> groupingAllBanksTransactions(Function<BankTransaction, String> grouping) {
        return bankTransactions.stream()
                .collect(Collectors.groupingBy(grouping, Collectors.summingDouble(BankTransaction::getAmount)));
    }
}
