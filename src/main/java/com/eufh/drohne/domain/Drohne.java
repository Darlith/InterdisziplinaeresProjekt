package com.eufh.drohne.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eufh.drohne.business.service.impl.OrderLocation;
import com.google.maps.model.LatLng;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
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
	@Transient
	private List<Route> route;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "startTime")
	private DateTime startTime;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "returnTime")
	private DateTime returnTime;

	@Column(name = "geschwindigkeit")
	public int speed = 60; // kmh

	@Column(name = "all_time_distance")
	private double allTimeDistance = 0;

	@Column(name = "all_time_flight_time")
	private double allTimeFlightTime = 0;

	@Column(name = "max_weight")
	private double maxWeight = 4;

	@Column(name = "max_distance")
	private double maxDistance = 25;

	@Column(name = "max_packages")
	private int maxPackages = 4;

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
	 */
	public void addPackage(Order order) {
		order.setDrone(this);
		this.orders.add(order);
		this.packageCount++;
		//Converting to Integer to avoid Floating Point Error
		int weight = (int)(order.getWeight() * 10);
		int totalWeight = (int)(this.totalPackageWeight * 10);
		this.totalPackageWeight = ((double)weight + totalWeight) / 10.0;
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
		this.startTime = null;
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
	

	public DateTime getStartTime() {
		return startTime;
	}

	public List<ProcessedOrder> start(DateTime simTime)
	{
		this.startTime = simTime.plusMinutes(5); // 5 minutes packaging time
		List<ProcessedOrder> poList = new ArrayList<ProcessedOrder>();
		for(Order o : this.orders)
		{
			ProcessedOrder po = new ProcessedOrder(o.getId(), o.getOrderDate(), o.getLocation(), o.getWeight(), simTime, this.id);
			String routeString = "";
			for(Route r : this.route)
			{
				if(r.getDestinationOrderLocation().getOrderID() == o.getId())
				{
					int minutes = (int) Math.floor(r.getDistance());
					int seconds = (int) Math.floor((r.getDistance() - minutes)*60);
					po.setDeliveryDate(simTime.plusMinutes(minutes).plusSeconds(seconds));
				}
				OrderLocation loc = r.getDestinationOrderLocation();
				LatLng latlng = r.getDestinationOrderLocation().getLatlng();
				routeString += latlng.lat + "|" + latlng.lng + "|" + loc.getAddress() + ",";
			}
			po.setRoute(routeString);
			poList.add(po);
			
		}
		// Set Analytics data
		double distance = this.getAllTimeDistance() + this.totalDistance;
		this.setAllTimeDistance(round(distance,2));
		int flightTime = Minutes.minutesBetween(this.startTime, this.returnTime).getMinutes();
		this.setAllTimeFlightTime(this.getAllTimeFlightTime() + flightTime);
		//return an Frontend
		//TEST CODE
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.printf("Drohne "+ this.id + " gestartet am " + fmt.print(this.startTime) + " mit " + this.packageCount + " Paket(en) mit " 
		+ this.totalPackageWeight + " Kilo auf einer Strecke von %.3f km.", this.totalDistance);
		System.out.println();
		System.out.println("Sie wird am " + fmt.print(this.returnTime) + " zur�ckerwartet. Es werden folgende Orte beliefert: ");
		for(int i = 0; i < route.size() -1; i++)
		{
			System.out.print((i+1) + ". " + route.get(i).getDestinationOrderLocation().getAddress());
			System.out.print(" (id=" + route.get(i).getDestinationOrderLocation().getOrderID() + ")");
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
		return poList;
	}

	@Override
	public String toString() {
		return "Drohne [id=" + id + ", totalPackageWeight=" + totalPackageWeight + ", packageCount=" + packageCount
				+ ", totalDistance=" + totalDistance + ", orders=" + orders + ", speed=" + speed + "]";
	}

	public double getAllTimeDistance() {
		return allTimeDistance;
	}

	public void setAllTimeDistance(double allTimeDistance) {
		this.allTimeDistance = allTimeDistance;
	}

	public double getAllTimeFlightTime() {
		return allTimeFlightTime;
	}

	public void setAllTimeFlightTime(double allTimeFlightTime) {
		this.allTimeFlightTime = allTimeFlightTime;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public int getMaxPackages() {
		return maxPackages;
	}

	public void setMaxPackages(int maxPackages) {
		this.maxPackages = maxPackages;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
