package com.app;

import com.app.parser.BankStatementCSVParser;
import com.app.parser.BankStatementParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BankStatementAnalyzer {
    private static final String DIRECTORY = "src/main/storage/";
    private static final BankStatementParser bankStatementCSVParser = new BankStatementCSVParser();

    public static void main(String[] args) throws IOException {
        BankStatementAnalyzer bankStatementAnalyzer = new BankStatementAnalyzer();
        bankStatementAnalyzer.analyze("transactions.txt", new BankStatementCSVParser());
        bankStatementAnalyzer.groupMonth("transactions.txt");
        bankStatementAnalyzer.groupCategory("transactions.txt");
    }

    public void analyze(String fileName, BankStatementParser bankStatementParser) throws IOException {
        Path path = Paths.get(DIRECTORY + fileName);
        List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(Files.readAllLines(path));
        BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
        collectSummary(bankStatementProcessor);

        System.out.println("Транзакции за период с максимальной суммой:");
        bankStatementProcessor.findMaxBankTransactionsForPeriod(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 31))
                .forEach(System.out::println);
        System.out.println("Транзакции за период с минимальной суммой:");
        bankStatementProcessor.findMinBankTransactionsForPeriod(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 31))
                .forEach(System.out::println);
    }

    private void collectSummary(BankStatementProcessor bankStatementProcessor) {
        System.out.println("All - " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("Month - " + bankStatementProcessor.calculateTotalInMonth(LocalDate.of(2021, 12, 1)));
        System.out.println("Category - " + bankStatementProcessor.calculateTotalForCategory("pepsi"));
    }

    private void groupMonth(String fileName) throws IOException {
        Map<String, Double> groupMonths = groupingAllBanksTransactions(fileName, bankTransaction -> bankTransaction.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")));
        groupMonths.forEach((k, v) -> System.out.printf("Месяц(%s) - %.3f%n", k.toUpperCase(Locale.ENGLISH), v));
    }

    private void groupCategory(String fileName) throws IOException {
        Map<String, Double> groupMonths = groupingAllBanksTransactions(fileName, BankTransaction::getDescription);
        groupMonths.forEach((k, v) -> System.out.printf("Категория(%s) - %.3f%n", k.toUpperCase(Locale.ENGLISH), v));
    }

    private Map<String, Double> groupingAllBanksTransactions(String fileName, Function<BankTransaction, String> grouping) throws IOException {
        Path path = Paths.get(DIRECTORY + fileName);
        List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(Files.readAllLines(path));
        return bankTransactions.stream().collect(Collectors.groupingBy(grouping, Collectors.summingDouble(BankTransaction::getAmount)));
    }
}
