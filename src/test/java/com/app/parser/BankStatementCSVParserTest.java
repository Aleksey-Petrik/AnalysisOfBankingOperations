package com.app.parser;

import com.app.BankTransaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankStatementCSVParserTest {

    @Test
    void parseFromCSV() {
        BankStatementParser bankStatementParser = new BankStatementCSVParser();
        BankTransaction actualBankTransaction = bankStatementParser.parseFromCSV("16-03-2022,3015.56,Test", ",");
        BankTransaction expectedBankTransaction = new BankTransaction(LocalDate.of(2022, 3, 16), 3015.56, "Test");

        assertEquals(expectedBankTransaction, actualBankTransaction, "Fail!!!");
    }
}