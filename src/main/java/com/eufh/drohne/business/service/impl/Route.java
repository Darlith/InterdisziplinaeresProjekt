package com.eufh.drohne.business.service.impl;

import org.joda.time.DateTime;

public class Route {
	private OrderLocation originOrderLocation;
	private OrderLocation destinationOrderLocation;
	private DateTime destinationOrderDate;
	private double distance;
	
	public Route(OrderLocation originOrderLocation, OrderLocation destinationOrderLocation, DateTime destinationOrderDate, double distance)
	{
		this.originOrderLocation = originOrderLocation;
		this.destinationOrderLocation = destinationOrderLocation;
		this.destinationOrderDate = destinationOrderDate;
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	
	public OrderLocation getOriginOrderLocation() {
		return originOrderLocation;
	}

	public OrderLocation getDestinationOrderLocation() {
		return destinationOrderLocation;
	}

	public DateTime getDestinationOrderDate() {
		return destinationOrderDate;
	}

	
}
