package com.app.parser;

import com.app.model.BankTransaction;

import java.util.List;

public interface BankStatementParser {
    BankTransaction parseFromCSV(String line, String separator);

    List<BankTransaction> parseLinesFromCSV(List<String> lines);
}
