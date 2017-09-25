package com.eufh.drohne.analytics;

import com.eufh.drohne.domain.Coordinates;

import java.util.ArrayList;
import java.util.Map;

public class AnalyticsDTO {

    private int processedOrders;

    private double averageFlightTime;

    private double delayedQuote;

    private int droneCount;

    private ArrayList<Coordinates> coordinates;

    private ArrayList<Integer> speedPerDrone;

    private ArrayList<Double> distancePerDrone;

    private ArrayList<Double> flightTimePerDrone;

    public AnalyticsDTO(int processedOrders, double averageFlightTime, double delayedQuote, int droneCount, ArrayList<Coordinates> coordinates, ArrayList<Integer> speedPerDrone, ArrayList<Double> distancePerDrone, ArrayList<Double> flightTimePerDrone) {
        this.processedOrders = processedOrders;
        this.averageFlightTime = averageFlightTime;
        this.delayedQuote = delayedQuote;
        this.droneCount = droneCount;
        this.coordinates = coordinates;
        this.speedPerDrone = speedPerDrone;
        this.distancePerDrone = distancePerDrone;
        this.flightTimePerDrone = flightTimePerDrone;
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

    public ArrayList<Integer> getSpeedPerDrone() {
        return speedPerDrone;
    }

    public ArrayList<Double> getDistancePerDrone() {
        return distancePerDrone;
    }

    public ArrayList<Double> getFlightTimePerDrone() {
        return flightTimePerDrone;
    }
}
