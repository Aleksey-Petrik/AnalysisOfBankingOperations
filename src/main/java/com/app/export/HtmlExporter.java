package com.app.export;

import com.app.SummaryStatistics;

public class HtmlExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        StringBuilder html = new StringBuilder().append("<!doctype html>")
                .append("<html lang='en'>")
                .append("<head><title>Bank Transaction Report</title></head>")
                .append("<body>")
                .append("<ul>")
                .append(String.format("<li><strong>The sum is</strong>: %.3f </li>", summaryStatistics.getSum()))
                .append(String.format("<li><strong>The average is</strong>: %.3f </li>", summaryStatistics.getAverage()))
                .append(String.format("<li><strong>The max is</strong>: %.3f </li>", summaryStatistics.getMax()))
                .append(String.format("<li><strong>The min is</strong>: %.3f </li>", summaryStatistics.getMin()))
                .append("</body>")
                .append("</ul>")
                .append("/html");
        return html.toString();
    }
}
