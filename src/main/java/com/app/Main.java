package com.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final String DIRECTORY = "src/main/storage/";

    public static void main(String[] args) throws IOException {

        BankStatementCSVParser bankStatementCSVParser = new BankStatementCSVParser();

        Path path = Paths.get(DIRECTORY + "transactions.txt");

        List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(Files.readAllLines(path));
        System.out.println(calculateTotalAmount(bankTransactions));
    }

    public static double calculateTotalAmount(List<BankTransaction> bankTransactions){
        return bankTransactions.stream().map(BankTransaction::getAmount).reduce(Double::sum).get();
    }
}
