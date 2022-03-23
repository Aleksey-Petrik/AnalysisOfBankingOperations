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

public class BankStatementAnalyzer {
    private static final String DIRECTORY = "src/main/storage/";
    private static final BankStatementParser bankStatementCSVParser = new BankStatementCSVParser();

    public static void main(String[] args) throws IOException {
        BankStatementAnalyzer bankStatementAnalyzer = new BankStatementAnalyzer();
        bankStatementAnalyzer.analyze("transactions.txt", new BankStatementCSVParser());

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

        bankStatementProcessor.groupingMonths().forEach((k, v) -> System.out.printf("Месяц(%s) - %.3f%n", k.toUpperCase(Locale.ENGLISH), v));
        bankStatementProcessor.groupingCategories().forEach((k, v) -> System.out.printf("Категория(%s) - %.3f%n", k.toUpperCase(Locale.ENGLISH), v));

        for (Map.Entry<String, Map<String, Double>> month : bankStatementProcessor.groupingMonthsForCategories().entrySet()) {
            System.out.println("-" + month.getKey());
            for (Map.Entry<String, Double> category : month.getValue().entrySet()) {
                System.out.println("----" + category.getKey() + " " + category.getValue());
            }
        }

        System.out.println("Amount >= 1000");
        bankStatementProcessor.findTransactions(bankTransaction -> bankTransaction.getAmount() >= 1000).forEach(System.out::println);
        System.out.println("Amount >= 1000 in month 12.2021");
        bankStatementProcessor.findTransactions(bankTransaction -> bankTransaction.getAmount() >= 1000
                        && bankTransaction.getDate().format(DateTimeFormatter.ofPattern("MM.yyyy")).equals("12.2021"))
                .forEach(System.out::println);

        System.out.printf("Суммирование с определенной суммы: %.3f", bankStatementProcessor.summationWithAmount(1000));
    }

    private void collectSummary(BankStatementProcessor bankStatementProcessor) {
        System.out.println("All - " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("Month - " + bankStatementProcessor.calculateTotalInMonth(LocalDate.of(2021, 12, 1)));
        System.out.println("Category - " + bankStatementProcessor.calculateTotalForCategory("pepsi"));
    }
}
