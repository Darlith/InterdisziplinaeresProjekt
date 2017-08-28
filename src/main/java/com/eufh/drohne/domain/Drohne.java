package com.eufh.drohne.domain;

public class Drohne {

	private int id;
	private double totalPackageWeight; //kg
	private int packageCount;
	private double totalDistance; //km
	private final int speed = 60; // kmh
	
	public Drohne() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;	
		this.setTotalDistance(0.0);
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
	public void addPackage(double weight) {
			this.packageCount++;
			this.totalPackageWeight += weight;
	}
	
	public void resetDrone() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;
		this.setTotalDistance(0.0);
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

}
