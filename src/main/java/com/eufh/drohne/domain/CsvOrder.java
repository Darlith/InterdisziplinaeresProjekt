package com.eufh.drohne.domain;

public class CsvOrder {

    private String orderTime;

    private String location;

    private String weight;

    public CsvOrder() {

    }

    public CsvOrder(String orderTime, String location, String weight) {
        this.orderTime = orderTime;
        this.location = location;
        this.weight = weight;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return this.orderTime + ", " + this.location + ", " + this.weight;
    }
}
