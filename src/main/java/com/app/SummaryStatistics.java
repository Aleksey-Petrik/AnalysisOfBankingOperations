package com.app;

public class SummaryStatistics {
    private final double sum;
    private final double max;
    private final double min;
    private final double average;

    private SummaryStatistics(Builder builder) {
        this.sum = builder.sum;
        this.max = builder.max;
        this.min = builder.min;
        this.average = builder.average;
    }

    public static class Builder {
        private double sum;
        private double max;
        private double min;
        private double average;

        public Builder sum(double sum) {
            this.sum = sum;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder average(double average) {
            this.average = average;
            return this;
        }

        public SummaryStatistics build() {
            return new SummaryStatistics(this);
        }
    }

    public double getSum() {
        return sum;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getAverage() {
        return average;
    }
}
