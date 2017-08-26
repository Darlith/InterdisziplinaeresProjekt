package com.eufh.drohne.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DROHNE")
public class Drohne {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	@Column(name = "LIEFERORT")
	private String lieferort;
	@Column(name = "GEWICHT")
	private String gewicht;
	private double totalPackageWeight; //kg
	private int packageCount;
	private double totalDistance; //km
	private final int speed = 60; // kmh
	
	public Drohne() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;	
		this.setTotalDistance(0.0);
	}
	
	public Drohne(int id, String lieferort, String gewicht){
		this.id = id;
		this.lieferort = lieferort;
		this.gewicht = gewicht;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLieferort() {
		return lieferort;
	}
	public void setLieferort(String lieferort) {
		this.lieferort = lieferort;
	}
	public String getGewicht() {
		return gewicht;
	}
	public void setGewicht(String gewicht) {
		this.gewicht = gewicht;
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
