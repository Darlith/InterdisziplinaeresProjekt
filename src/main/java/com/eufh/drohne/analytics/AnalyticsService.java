package com.eufh.drohne.analytics;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.ProcessedOrder;
import org.joda.time.Minutes;

import java.util.ArrayList;

public class AnalyticsService {

    private TestService coordinatesService;

    public AnalyticsService(TestService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    public AnalyticsDTO getAnalyzedData(ArrayList<ProcessedOrder> orders, ArrayList<Drohne> drones) {
        int processedOrders = orders.size();
        if (processedOrders == 0) {
            return new AnalyticsDTO(0, 0, 0, 0, null);
        }
        double averageFlightTime = this.getAverageFlightTime(orders);
        double delayedQuote = this.getDelayedQoute(orders) * 100;
        ArrayList<Coordinates> coordinates = this.getCoordinates(orders);

        return new AnalyticsDTO(processedOrders, averageFlightTime, delayedQuote, drones.size(), coordinates);
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
