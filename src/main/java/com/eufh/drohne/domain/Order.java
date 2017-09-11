package com.eufh.drohne.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;


public class Order {
	
	private int id;
	
	private DateTime orderDate;
	
	private String location;
	
	private double weight;

	//Default Constructor
	public Order()
	{
	}
	
	public Order(DateTime orderDate, String location, double weight)
	{
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
}
