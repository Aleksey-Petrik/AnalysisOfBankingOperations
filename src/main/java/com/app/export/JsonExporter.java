package com.app.export;

import com.app.SummaryStatistics;

public class JsonExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        StringBuilder json = new StringBuilder();
        json.append("{\n")
                .append("\t\"title\" : \"Bank Transaction Report\"\n")
                .append(String.format("\t\"sum\" : %.3f%n", summaryStatistics.getSum()))
                .append(String.format("\t\"average\" : %.3f%n", summaryStatistics.getAverage()))
                .append(String.format("\t\"min\" : %.3f%n", summaryStatistics.getMax()))
                .append(String.format("\t\"max\" : %.3f%n", summaryStatistics.getMin()))
                .append("}\n");
        return json.toString();
    }
}
