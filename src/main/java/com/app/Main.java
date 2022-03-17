package com.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static final String DIRECTORY = "src/main/storage/";
    private static final BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(DIRECTORY + "transactions.txt");

        List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(Files.readAllLines(path));
        BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

        System.out.println("All - " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("Month - " + bankStatementProcessor.calculateTotalInMonth(LocalDate.of(2021, 12, 1)));
        System.out.println("Category - " + bankStatementProcessor.calculateTotalForCategory("pepsi"));
    }
}
