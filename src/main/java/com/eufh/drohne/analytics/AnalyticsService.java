package com.eufh.drohne.analytics;

import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.ProcessedOrder;
import org.joda.time.Minutes;

import java.util.ArrayList;

public class AnalyticsService {

    public AnalyticsDTO getAnalyzedData(ArrayList<ProcessedOrder> orders, ArrayList<Drohne> drones) {
        int processedOrders = orders.size();
        if (processedOrders == 0) {
            return new AnalyticsDTO(0, 0, 0, 0);
        }
        double averageFlightTime = this.getAverageFlightTime(orders);
        double delayedQuote = this.getDelayedQoute(orders) * 100;

        return new AnalyticsDTO(processedOrders, averageFlightTime, delayedQuote, drones.size());
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
