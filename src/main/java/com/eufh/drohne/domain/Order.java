package com.eufh.drohne.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name = "ORDERS")
public class Order {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	@Column(name = "OrderDate")
	private DateTime orderDate;
	@Column(name = "Location")
	private String location;
	@Column(name = "Weight")
	private double weight;

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
	public DateTime getOrderDateById(int id)
	{
		if(this.id == id)
		{
			return this.orderDate;
		}
		return null;
	}
}
