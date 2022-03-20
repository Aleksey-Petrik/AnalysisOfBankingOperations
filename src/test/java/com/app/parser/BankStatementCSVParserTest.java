package com.app.parser;

import com.app.BankStatementProcessor;
import com.app.BankTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankStatementCSVParserTest {
    private final BankStatementParser bankStatementParser = new BankStatementCSVParser();
    private List<BankTransaction> actualBankTransactionList = null;
    private BankStatementProcessor bankStatementProcessor = null;

    @BeforeEach
    void setup() {
        List<String> dataList = new ArrayList<>();
        dataList.add("01-01-2022,1500.00,pepsi");
        dataList.add("21-12-2021,300.00,pepsi");
        dataList.add("22-12-2021,500.00,pepsi");
        dataList.add("23-12-2021,1500.00,pepsi");
        dataList.add("11-12-2021,300.00,cola");
        dataList.add("12-12-2021,500.00,cola");
        dataList.add("13-12-2021,1000.00,cola");
        dataList.add("10-12-2021,1500.00,cola");
        dataList.add("09-12-2021,300.00,cola");
        dataList.add("21-11-2021,340.00,test");
        dataList.add("22-11-2021,550.00,test");
        dataList.add("23-11-2021,1200.00,test");
        dataList.add("20-11-2022,1100.00,test");
        actualBankTransactionList = bankStatementParser.parseLinesFromCSV(dataList);
        bankStatementProcessor = new BankStatementProcessor(actualBankTransactionList);
    }

    @Test
    void parseFromCSV() {
        BankStatementParser bankStatementParser = new BankStatementCSVParser();
        BankTransaction actualBankTransaction = bankStatementParser.parseFromCSV("16-03-2022,3015.56,Test", ",");
        BankTransaction expectedBankTransaction = new BankTransaction(LocalDate.of(2022, 3, 16), 3015.56, "Test");

        assertEquals(expectedBankTransaction, actualBankTransaction, "Fail!!!");
    }

    @Test
    void size() {
        assertEquals(13, actualBankTransactionList.size());
    }

    @Test
    void findMaxBankTransactionsForPeriod() {
        List<BankTransaction> actualBankTransactionList = bankStatementProcessor.findMaxBankTransactionsForPeriod(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 31));
        assertEquals(2, actualBankTransactionList.size());
    }

    @Test
    void findMinBankTransactionsForPeriod() {
        List<BankTransaction> actualBankTransactionList = bankStatementProcessor.findMinBankTransactionsForPeriod(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 31));
        assertEquals(3, actualBankTransactionList.size());
    }

}