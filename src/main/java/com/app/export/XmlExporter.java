package com.app.export;

import com.app.SummaryStatistics;

public class XmlExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<statistic>")
                .append("title lang=\"en\">Bank Transaction Report</title>")
                .append(String.format("<sum>%.3f</sum>", summaryStatistics.getSum()))
                .append(String.format("<average>%.3f</average>", summaryStatistics.getAverage()))
                .append(String.format("<min>%.3f</min>", summaryStatistics.getMax()))
                .append(String.format("<max>%.3f</max>", summaryStatistics.getMin()))
                .append("</statistic>");
        return xml.toString();
    }
}
