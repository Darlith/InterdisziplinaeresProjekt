package com.eufh.drohne.domain;

import org.joda.time.DateTime;

public class ProcessedOrder {
	private int id;
	private DateTime orderDate;
	private String location;
	private double weight;
	DateTime processedDate;
	DateTime deliveryDate;
	int droneId;
	boolean delayed;

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
	
	
}
