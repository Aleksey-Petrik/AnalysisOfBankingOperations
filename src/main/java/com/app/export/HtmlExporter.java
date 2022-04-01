package com.app.export;

import com.app.SummaryStatistics;

public class HtmlExporter implements Exporter {
    @Override
    public String export(SummaryStatistics summaryStatistics) {
        StringBuilder html = new StringBuilder().append("<!doctype html>")
                .append("<html lang='en'>\n")
                .append("<head><title>Bank Transaction Report</title></head>\n")
                .append("<body>\n")
                .append("\t<ul>\n")
                .append(String.format("\t\t<li><strong>The sum is</strong>: %.3f </li>%n", summaryStatistics.getSum()))
                .append(String.format("\t\t<li><strong>The average is</strong>: %.3f </li>%n", summaryStatistics.getAverage()))
                .append(String.format("\t\t<li><strong>The max is</strong>: %.3f </li>%n", summaryStatistics.getMax()))
                .append(String.format("\t\t<li><strong>The min is</strong>: %.3f </li>%n", summaryStatistics.getMin()))
                .append("\t</ul>\n")
                .append("</body>\n")
                .append("/html\n");
        return html.toString();
    }
}
