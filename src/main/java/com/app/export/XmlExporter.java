package com.app.export;

import com.app.SummaryStatistics;

public class XmlExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<statistic>\n")
                .append("\ttitle lang=\"en\">Bank Transaction Report</title>\n")
                .append(String.format("\t<sum>%.3f</sum>%n", summaryStatistics.getSum()))
                .append(String.format("\t<average>%.3f</average>%n", summaryStatistics.getAverage()))
                .append(String.format("\t<min>%.3f</min>%n", summaryStatistics.getMax()))
                .append(String.format("\t<max>%.3f</max>%n", summaryStatistics.getMin()))
                .append("</statistic>\n");
        return xml.toString();
    }
}
