package com.eufh.drohne.analytics;

public class AnalyticsDTO {

    private int processedOrders;

    private double averageFlightTime;

    private double delayedQuote;

    private int droneCount;

    public AnalyticsDTO(int processedOrders, double averageFlightTime, double delayedQuote, int droneCount) {
        this.processedOrders = processedOrders;
        this.averageFlightTime = averageFlightTime;
        this.delayedQuote = delayedQuote;
        this.droneCount = droneCount;
    }

    public int getProcessedOrders() {
        return processedOrders;
    }

    public double getAverageFlightTime() {
        return averageFlightTime;
    }

    public double getDelayedQuote() {
        return delayedQuote;
    }

    public int getDroneCount() {
        return droneCount;
    }
}
