package com.eufh.drohne.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Coordinates")
public class Coordinates {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	@Column(name = "Location")
	private String location;
	@Column(name = "Latitude")
	private double latitude;
	@Column(name = "Longitude")
	private double longitude;
	
	public int getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public Coordinates(String location, double latitude, double longitude) {
		this.location = location;
		this.latitude = latitude; 
		this.longitude = longitude;
	}

}
