package com.eufh.drohne.analytics;

import com.eufh.drohne.domain.Coordinates;

import java.util.ArrayList;

public class AnalyticsDTO {

    private int processedOrders;

    private double averageFlightTime;

    private double delayedQuote;

    private int droneCount;

    private ArrayList<Coordinates> coordinates;

    public AnalyticsDTO(int processedOrders, double averageFlightTime, double delayedQuote, int droneCount, ArrayList<Coordinates> coordinates) {
        this.processedOrders = processedOrders;
        this.averageFlightTime = averageFlightTime;
        this.delayedQuote = delayedQuote;
        this.droneCount = droneCount;
        this.coordinates = coordinates;
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

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
    }
}
