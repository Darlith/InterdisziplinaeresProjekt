package com.eufh.drohne.analytics;

import com.eufh.drohne.business.service.CoordinateService;
import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.ProcessedOrder;
import org.joda.time.Minutes;

import java.util.ArrayList;

public class AnalyticsService {

    private CoordinateService coordinatesService;

    public AnalyticsService(CoordinateService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    public AnalyticsDTO getAnalyzedData(ArrayList<ProcessedOrder> orders, ArrayList<Drohne> drones) {
        int processedOrders = orders.size();
        if (processedOrders == 0) {
            return new AnalyticsDTO(0, 0, 0, 0, null, null, null, null);
        }
        double averageFlightTime = this.getAverageFlightTime(orders);
        double delayedQuote = this.getDelayedQoute(orders) * 100;
        ArrayList<Coordinates> coordinates = this.getCoordinates(orders);
        ArrayList<Double> distances = this.getDistancePerDrone(drones);
        ArrayList<Integer> speed = this.getSpeedPerDrone(drones);
        ArrayList<Double> flightTimes = this.getFlightTimePerDrone(drones);


        return new AnalyticsDTO(processedOrders, averageFlightTime, delayedQuote, drones.size(), coordinates, speed, distances, flightTimes);
    }

    private ArrayList<Integer> getSpeedPerDrone(ArrayList<Drohne> drones) {
        ArrayList<Integer> speed = new ArrayList<>();
        for(Drohne drone: drones) {
            speed.add(drone.getSpeed());
        }

        return speed;
    }

    private ArrayList<Double> getDistancePerDrone(ArrayList<Drohne> drones) {
        ArrayList<Double> distances = new ArrayList<>();
        for(Drohne drone: drones) {
            distances.add(drone.getAllTimeDistance());
        }

        return distances;
    }

    private ArrayList<Double> getFlightTimePerDrone(ArrayList<Drohne> drones) {
        ArrayList<Double> times = new ArrayList<>();
        for(Drohne drone: drones) {
            times.add(drone.getAllTimeFlightTime());
        }

        return times;
    }

    private ArrayList<Coordinates> getCoordinates(ArrayList<ProcessedOrder> orders) {
        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();
        for(ProcessedOrder order: orders) {
            Coordinates c = this.coordinatesService.findOne(order.getLocation());
            if (!coordinates.contains(c)) {
                coordinates.add(c);
            }
        }
        return coordinates;
    }

    private double getDelayedQoute(ArrayList<ProcessedOrder> orders) {
        int totalOrders = orders.size();
        int delayedOrders = 0;
        for(ProcessedOrder order: orders) {
            if(order.getDelayed()) {
                delayedOrders++;
            }
        }
        return ((double)delayedOrders) / totalOrders;
    }

    private double getAverageFlightTime(ArrayList<ProcessedOrder> orders) {
        Minutes totalFlightTime = Minutes.ZERO;
        for(ProcessedOrder order: orders) {
            Minutes flightTime = Minutes.minutesBetween(order.getOrderDate(), order.getDeliveryDate());
            totalFlightTime = totalFlightTime.plus(flightTime);
        }
        int minutes = totalFlightTime.getMinutes();
        int totalOrders = orders.size();
        return ((double)minutes) / totalOrders;
    }

}
