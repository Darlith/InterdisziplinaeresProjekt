package com.eufh.drohne.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ProcessedOrder")
public class ProcessedOrder {
	@Id
	private int id;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "orderDate")
	private DateTime orderDate;
	@Column(name = "location")
	private String location;
	@Column(name = "weight")
	private double weight;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "processedDate")
	DateTime processedDate;
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "deliveryDate")
	DateTime deliveryDate;
	@Column(name = "droneId")
	int droneId;
	@Column(name = "delayed")
	boolean delayed;
	@Column(name = "route")
	private String route;

	
	public ProcessedOrder() {
		
	}

	public ProcessedOrder(int id, DateTime orderDate, String location, double weight, DateTime processedDate, int droneId) {
		this.id = id;
		this.orderDate = orderDate;
		this.location = location;
		this.weight = weight;
		this.processedDate = processedDate;
		this.droneId = droneId;
	}

	public void setDeliveryDate(DateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
		if(deliveryDate.isBefore(this.orderDate.plusHours(1)))
		{
			this.delayed = false;
		}
		else
		{
			this.delayed = true;
		}
		
		
	}

	public int getId() {
		return id;
	}

	public boolean isDelayed() {
		return delayed;
	}

	public DateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(DateTime orderDate) {
		this.orderDate = orderDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public DateTime getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(DateTime processedDate) {
		this.processedDate = processedDate;
	}

	public int getDroneId() {
		return droneId;
	}

	public void setDroneId(int droneId) {
		this.droneId = droneId;
	}

	public DateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDelayed(boolean delayed) {
		this.delayed = delayed;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}
}
