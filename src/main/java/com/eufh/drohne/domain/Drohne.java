package com.eufh.drohne.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;

@Entity
@Table(name = "Drone")
public class Drohne {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "gewicht")
	private double totalPackageWeight; //kg
	@Column(name = "pakete")
	private int packageCount;
	@Column(name = "distanz")
	private double totalDistance; //km
	@Transient
	private List<Order> orders = new ArrayList<Order>();
	@Column(name = "geschwindigkeit")
	public int speed = 60; // kmh
	
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
		order.setDrone(this);
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
		System.out.println("Drohne gestartet mit" + this.packageCount + "Paketen mit " + this.totalPackageWeight
				+ "Kilo auf einer Strecke von " + this.totalDistance + "\\. Es werden folgende Orte beliefert: ");
		this.resetDrone();
	}
}
