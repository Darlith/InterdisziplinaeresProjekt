package com.eufh.drohne.domain;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;


public class Order {
	
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
	
	private int id;
	
	private DateTime orderDate;
	
	private String location;
	
	private double weight;
	
	private Drohne drone;

	//Default Constructor
	public Order()
	{
	}
	
	public Order(DateTime orderDate, String location, double weight)
	{
		this.id = ID_GENERATOR.getAndIncrement();
		this.orderDate = orderDate;
		this.location = location;
		this.weight = weight;
	}
	
	public DateTime getOrderDate() {
		return this.orderDate;
	}
	/** Sets the Date in the
	 * 
	 * @param cal 
	 */
	public void setOrderDate(DateTime orderDate) {
		this.orderDate = orderDate;
		//Konvertierung f�r SQL Statement in sql.Date
		//this.orderDate = new Date(cal.getTimeInMillis());
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
	public int getId() {
		return id;
	}
	
	public Order getOrder(int id)
	{
		if(this.id == id)
		{
			return this;
		}
		return null;
	}

	public Drohne getDrone() {
		return drone;
	}

	public void setDrone(Drohne drone) {
		this.drone = drone;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", location=" + location + ", weight=" + weight
				+ ", drone=" + drone + "]";
	}
}
