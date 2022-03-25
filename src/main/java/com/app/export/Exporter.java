package com.app.export;

import com.app.SummaryStatistics;

public interface Exporter {
    String export(SummaryStatistics summaryStatistics);
}
