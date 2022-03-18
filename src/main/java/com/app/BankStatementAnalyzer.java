package com.app;

import com.app.parser.BankStatementCSVParser;
import com.app.parser.BankStatementParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

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
    }

    private void collectSummary(BankStatementProcessor bankStatementProcessor) {
        System.out.println("All - " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("Month - " + bankStatementProcessor.calculateTotalInMonth(LocalDate.of(2021, 12, 1)));
        System.out.println("Category - " + bankStatementProcessor.calculateTotalForCategory("pepsi"));
    }
}
