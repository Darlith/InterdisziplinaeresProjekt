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
	@Column(name = "Location")
	private String location;
	@Column(name = "Latitude")
	private double latitude;
	@Column(name = "Longitude")
	private double longitude;
	
	public String getLocation() {
		return location;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	
	
	@Override
	public String toString() {
		return "Coordinates [location=" + location + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	public Coordinates()
	{
		//default
	}
	public Coordinates(String location, double latitude, double longitude) {
		this.location = location;
		this.latitude = latitude; 
		this.longitude = longitude;
	}
}
