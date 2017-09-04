package com.eufh.drohne.domain;

import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;

public class Drohne {

	private int id;
	private double totalPackageWeight; //kg
	private int packageCount;
	private double totalDistance; //km
	private List<Order> orders;
	private final int speed = 60; // kmh
	
	public Drohne() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;	
		this.setTotalDistance(0.0);
	}
	
	public List<Order> getOrders() {
		return orders;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Adds a package to the drone
	 * 
	 * @param weight Weight of the new package
	 */
	public void addPackage(Order order) {
		this.orders.add(order);
		this.packageCount++;
		this.totalPackageWeight += order.getWeight();
	}
	
	public void removePackage(Order order) {
		this.orders.remove(order);
		this.packageCount--;
		this.totalPackageWeight -= order.getWeight();
	}
	
	public void resetDrone() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;
		this.setTotalDistance(0.0);
		this.orders.clear();
	}
	
	public Drohne findDroneById(int id)
	{
		if(this.id == id)
		{
			return this;
		}
		return null;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
	
	public double getTotalPackageWeight() {
		return totalPackageWeight;
	}
	
	public int getPackageCount() {
		return packageCount;
	}
	
	public void start(DateTime simTime)
	{
		//return an Frontend
		//TEST CODE
		System.out.println("Drohne gestartet mit" + this.packageCount + "Paketen mit" + this.totalPackageWeight
				+ "Kilo auf einer Strecke von" + this.totalDistance + "\\. Es werden folgende Orte beliefert: ");
		this.resetDrone();
	}
}
