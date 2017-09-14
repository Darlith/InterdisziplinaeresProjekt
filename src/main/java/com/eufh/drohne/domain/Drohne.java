package com.eufh.drohne.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.eufh.drohne.business.service.impl.Route;

@Entity
@Table(name = "Drone")
public class Drohne {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "gewicht")
	private double totalPackageWeight; //kg
	@Column(name = "pakete")
	private int packageCount;
	@Column(name = "distanz")
	private double totalDistance; //km
	@Transient
	private List<Order> orders;
	@Column(name = "geschwindigkeit")
	public int speed = 60; // kmh
	@Transient
	private List<Route> route;
	@Transient
	private DateTime returnTime;
	
	
	public Drohne() {
		this.route = new ArrayList<Route>();
		this.orders = new ArrayList<Order>();
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;	
		this.setTotalDistance(0.0);
	}
	
	public List<Order> getOrders() {
		return orders;
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
	public void addPackage(Order order) {
		order.setDrone(this);
		this.orders.add(order);
		this.packageCount++;
		this.totalPackageWeight += order.getWeight();
	}
	
	public void removePackage(Order order) {
		this.orders.remove(order);
		this.packageCount--;
		this.totalPackageWeight -= order.getWeight();
	}
	
	public void resetDrone() {
		this.totalPackageWeight = 0.0;
		this.packageCount = 0;
		this.setTotalDistance(0.0);
		this.orders.clear();
		this.route.clear();
		this.returnTime = null;
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
	
	
	
	public DateTime getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(DateTime returnTime) {
		this.returnTime = returnTime;
	}

	public double getTotalPackageWeight() {
		return totalPackageWeight;
	}
	
	public int getPackageCount() {
		return packageCount;
	}
	

	public void setRoute(List<Route> route) {
		this.route = route;
	}
	
	public List<Route> getRoute()
	{
		return this.route;
	}

	public void start(DateTime simTime)
	{
		List<ProcessedOrder> poList = new ArrayList<ProcessedOrder>();
		for(Order o : this.orders)
		{
			ProcessedOrder po = new ProcessedOrder(o.getId(), o.getOrderDate(), o.getLocation(), o.getWeight(), simTime, this.id);
			for(Route r : this.route)
			{
				if(r.getDestinationOrderLocation().getOrderID() == o.getId())
				{
					int minutes = (int) Math.floor(r.getDistance());
					int seconds = (int) Math.floor((r.getDistance() - minutes)*60);
					po.setDeliveryDate(simTime.plusMinutes(minutes).plusSeconds(seconds));
				}
			}
			poList.add(po);
		}
		//return an Frontend
		//TEST CODE
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.printf("Drohne "+ this.id + " gestartet am " + fmt.print(simTime) + " mit " + this.packageCount + " Paket(en) mit " 
		+ this.totalPackageWeight + " Kilo auf einer Strecke von %.3f km.", this.totalDistance);
		System.out.println();
		System.out.println("Sie wird am " + fmt.print(simTime) + " zurückerwartet. Es werden folgende Orte beliefert: ");
		for(int i = 0; i < route.size() -1; i++)
		{
			System.out.print((i+1) + ". " + route.get(i).getDestinationOrderLocation().getAddress());
			for(ProcessedOrder po : poList)
			{
				if(po.getId() == route.get(i).getDestinationOrderLocation().getOrderID())
				{
					if(po.isDelayed())
					{
						System.out.print(" DELAYED\n");
					}
					else
					{
						System.out.print("\n");
					}
				}
			}
		}
		
	}

	@Override
	public String toString() {
		return "Drohne [id=" + id + ", totalPackageWeight=" + totalPackageWeight + ", packageCount=" + packageCount
				+ ", totalDistance=" + totalDistance + ", orders=" + orders + ", speed=" + speed + "]";
	}
	
	
}
