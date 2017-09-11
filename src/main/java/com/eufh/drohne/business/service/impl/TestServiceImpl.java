package com.eufh.drohne.business.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;
import com.eufh.drohne.repository.TestRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class TestServiceImpl implements TestService {
	
	GeoApiContext geoContext;
	DateTime simTime;
	Order[] order;
	int nextOrder;
	Drohne[] drones;
	Drohne activeDrone;
	
	private TestRepository testRepository;
	private TestService testService;
	
	public TestServiceImpl(TestRepository repo, TestService testService) {
		this.testRepository = repo;
		this.testService = testService;
	}

	@Override
	public ArrayList<Coordinates> findAll() {
		return testRepository.findAll();
	}

	@Override
	public Coordinates findOne(String id) {
		return testRepository.findOne(id);
	}

	@Override
	@Transactional
	public Coordinates save(Coordinates coordinates) {
		return testRepository.save(coordinates);
	}
	
	/*
	 * Entry point for the drone simulation
	 */
	public void startDroneSimulation() {
		//TODO PHKO: expand method as needed (DoSomething)
		String [] input = new String[] { "20.01.2017, 08:00, Strete, 2.1", "20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7", "20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7" };
		this.order = new Order[input.length];
		CreateOrderByList(input);
		this.nextOrder = 0;
		this.drones = new Drohne[] {new Drohne(), new Drohne(), new Drohne(), new Drohne(), new Drohne()};
		SetNextDroneActive();
		setSimTime();
		
		
		Simulate();
	}

	private void CreateOrderByList(String[] input) { 
		for(int i = 0; i < input.length; i++) {
			String[] orderArr = input[i].split(",");
			String[] date = orderArr[0].trim().split("\\.");
			String[] time = orderArr[1].trim().split(":");
			DateTime orderDate = new DateTime(Integer.parseInt(date[2]), Integer.parseInt(date[1]), 
					Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
			double weight = Double.parseDouble(orderArr[3].trim());
			this.order[i] = new Order(orderDate, orderArr[2].trim(), weight);
		}
	}

	private void SetNextDroneActive() {
		if (activeDrone == null || activeDrone.getId() == 5)
		{
			this.activeDrone = drones[0];
		}
		else
		{
			activeDrone = drones[activeDrone.getId()];
		}
		activeDrone.resetDrone();
	}

	private void setSimTime() {
		simTime = order[0].getOrderDate().minusMinutes(1);
	}

	private void Simulate() {
		while (nextOrder < order.length) {
		AddMinute();
		AddOrder();
		CheckMaximumTime();
		}
		activeDrone.start(simTime);
	}

	private void AddMinute() {
		simTime = simTime.plusMinutes(1);
	}
	
	private void AddOrder() {
		if(simTime.equals(order[nextOrder].getOrderDate())) 
		{
			if(order[nextOrder].getWeight() > 4.0)
			{
				//TODO: Paket ist schwerer als erlaubt, Error display
			}
			if((activeDrone.getTotalPackageWeight() + order[nextOrder].getWeight()) <= 4.0)
			{
				if(calcDroneRoutes(order[nextOrder]))
				{
					activeDrone.addPackage(order[nextOrder]);
				}
				
				
			}
			// TODO: else Starte aktive Drohne und f�ge aktuelles Paket zu neuer Drohne hinzu
			else
			{
				activeDrone.start(simTime);
				SetNextDroneActive();
				calcDroneRoutes(order[nextOrder]);
				activeDrone.addPackage(order[nextOrder]);
			}	
		}
		nextOrder++;
	}

	private void CheckMaximumTime() {
		// TODO Auto-generated method stub
		
	}
	

	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private boolean calcDroneRoutes(Order currentOrder) {
		List<OrderLocation> orderLocations = new ArrayList<OrderLocation>();
		List<OrderLocation> currentOrderLocations = new ArrayList<OrderLocation>();
		List<Route> routes = new ArrayList<Route>();
		currentOrderLocations.add(new OrderLocation(0, "Salcombe", new LatLng(50.2375800, -3.7697910)));
		List<Order> orders = activeDrone.getOrders();
		List<OrderLocation> queryOrderLocations = new ArrayList<OrderLocation>();
		orders.add(currentOrder);
		for(Order order : orders)
		{
			orderLocations.add(new OrderLocation(order.getId(), order.getLocation()));
		}
		for(OrderLocation ol : orderLocations)
		{
			Coordinates coordinates = testService.findOne(ol.getAddress());
			if(coordinates != null)
			{
				ol.setLatlng(new LatLng(coordinates.getLatitude(), coordinates.getLongitude()));
				currentOrderLocations.add(ol);
			}
			else 
			{
				queryOrderLocations.add(ol);
			}
		}
		if(!queryOrderLocations.isEmpty())
		{
			geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
			try {
				//TODO: Initialize mit 0, sobald die bekannten LatLngs in der DB sind
				for (int i = 0; i < queryOrderLocations.size(); i++)
				{
					OrderLocation o = queryOrderLocations.get(i);
					LatLng latLng = getLatLng(o.getAddress());
					o.setLatlng(latLng);
					currentOrderLocations.add(o);
					Coordinates c = new Coordinates(o.getAddress(), o.getLatlng().lat, o.getLatlng().lng);
					testService.save(c);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < currentOrderLocations.size(); i++) 
		{
			for (int j = i + 1; j < currentOrderLocations.size(); j++) 
			{
				double distance = Haversine.getDistance(currentOrderLocations.get(i).getLatlng().lat,currentOrderLocations.get(i).getLatlng().lng,
						currentOrderLocations.get(j).getLatlng().lat, currentOrderLocations.get(j).getLatlng().lng);
				DateTime originOrderDate = null;
				DateTime destinationOrderDate = null;
				if(currentOrderLocations.get(i).getAddress() != "Salcombe")
				{			
					for (Order o : orders)
					{
						Order searchOrder = o.findOne(currentOrderLocations.get(i).getOrderID());
						if(searchOrder != null);
						{
							originOrderDate = searchOrder.getOrderDate();
						}
					}
				}
				if(currentOrderLocations.get(j).getAddress() != "Salcombe")
				{
					for (Order o : orders)
					{
						Order searchOrder = o.findOne(currentOrderLocations.get(j).getOrderID());
						if(searchOrder != null);
						{
							destinationOrderDate = searchOrder.getOrderDate();
						}
					}
				}
				routes.add(new Route(currentOrderLocations.get(i), currentOrderLocations.get(j), destinationOrderDate , distance));
				routes.add(new Route(currentOrderLocations.get(j), currentOrderLocations.get(i), originOrderDate , distance));
				//CODE FOR TESTING
				int distanceKm = (int) distance;
				int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
				System.out.println("Distance from " + currentOrderLocations.get(i).getAddress() + " to " + currentOrderLocations.get(j).getAddress() + " : "
						+ distanceKm + "km " + distanceMeters + "m");
				//CODE FOR TESTING
			}
		}
		//Beste Route ausw�hlen
		double bestDistance = Double.MAX_VALUE;
		Time bestTime = new Time();
		List<Route> bestRoute = new ArrayList<Route>();
		List<ArrayList<Route>> currentRoutes = new ArrayList<ArrayList<Route>>();
		for(Order order : orders)
		{
			List<Route> tempRoutes = new ArrayList<Route>();
			for (Route r : routes)
			{
				tempRoutes.add(r);
			}
			for(Route route: tempRoutes)
			{
				if(route.getOriginOrderLocation().getAddress() == "Salcombe" 
						&& route.getDestinationOrderLocation().getAddress() == order.getLocation())
				{
					for (int i = 0; i < ((orders.size() -1) * (orders.size() - 2)); i++)
					{
						currentRoutes.add(new ArrayList<Route>(Arrays.asList(route)));
					}
					if(currentRoutes.size() == 0)
					{
						currentRoutes.add(new ArrayList<Route>(Arrays.asList(route)));
					}
					tempRoutes.remove(route);
				}
				else if(route.getOriginOrderLocation().getAddress() == "Salcombe" 
						|| route.getDestinationOrderLocation().getAddress() == order.getLocation())
				{
					tempRoutes.remove(route);
				}
			}
			//currentRoutes = 1 wenn ordersize 1 & 2, = 2 wenn ordersize 3, = 6 wenn ordersize 4
			if(orders.size() == 1)
			{
				currentRoutes.get(0).add(tempRoutes.get(0));
			}
			else
			{
				List<Order> tempOrders = new ArrayList<Order>();
				for (Order o : orders)
				{
					tempOrders.add(o);
				}
				tempOrders.remove(order);
				for(int i = 0; i < tempOrders.size(); i++)
				{
					List<Route> temptempRoutes = new ArrayList<Route>();
					for (Route r : tempRoutes)
					{
						temptempRoutes.add(r);
					}
					for (Route route : temptempRoutes)
					{
						if(route.getOriginOrderLocation().getAddress() == order.getLocation() 
								&& route.getDestinationOrderLocation().getAddress() == tempOrders.get(i).getLocation())
						{
							currentRoutes.get(i).add(route);
							if(orders.size() == 4)
							{
								currentRoutes.get(i+3).add(route);
							}
							temptempRoutes.remove(route);
						}
						else if(route.getOriginOrderLocation().getAddress() == order.getLocation() 
								|| route.getDestinationOrderLocation().getAddress() == tempOrders.get(i).getLocation())
						{
							temptempRoutes.remove(route);
						}
					}
					if(orders.size() == 2)
					{
						currentRoutes.get(i).add(temptempRoutes.get(0));
					}
					else
					{
						List<Order> temptempOrders = new ArrayList<Order>();
						for (Order o : tempOrders)
						{
							temptempOrders.add(o);
						}
						temptempOrders.remove(tempOrders.get(i));
						for(Order temptempOrder : temptempOrders)
						{
							List<Route> temp3Routes = new ArrayList<Route>();
							for (Route r : temptempRoutes)
							{
								temp3Routes.add(r);
							}
							for(Route route: temp3Routes)
							{
								if(route.getOriginOrderLocation().getAddress() == tempOrders.get(i).getLocation() 
								&& route.getDestinationOrderLocation().getAddress() == temptempOrder.getLocation())
								{
									currentRoutes.get(i).add(route);
									if(orders.size() == 4)
									{
										currentRoutes.get(i+3).add(route);
									}
									temp3Routes.remove(route);
								}
								else if(route.getOriginOrderLocation().getAddress() == tempOrders.get(i).getLocation() 
										|| route.getDestinationOrderLocation().getAddress() == temptempOrder.getLocation())
								{
									temptempRoutes.remove(route);
								}
							}
							if(orders.size() == 3)
							{
								currentRoutes.get(i).add(temp3Routes.get(0));
							}
							else
							{
								List<Order> temp3Orders = new ArrayList<Order>();
								for (Order o : temptempOrders)
								{
									temp3Orders.add(o);
								}
								temp3Orders.remove(temptempOrder);
								for(int j = 0; j < currentRoutes.size(); j++)
								{
									List<Route> temp4Routes = new ArrayList<Route>();
									for (Route r : temp3Routes)
									{
										temp4Routes.add(r);
									}
									for(Route route : temp4Routes)
									{
										if(route.getOriginOrderLocation().getAddress() == currentRoutes.get(j).get(2).getDestinationOrderLocation().getAddress()
												&& route.getDestinationOrderLocation().getAddress() == temp3Orders.get(0).getLocation())
										{
											currentRoutes.get(j).add(route);
											temp4Routes.remove(route);
										}
										else if(route.getOriginOrderLocation().getAddress() == currentRoutes.get(j).get(2).getDestinationOrderLocation().getAddress()
												|| route.getDestinationOrderLocation().getAddress() == temp3Orders.get(0).getLocation())
										{
											temp4Routes.remove(route);
										}
									}
									currentRoutes.get(j).add(temp4Routes.get(0));
								}
							}
						}					
					}
				}
			}
		}
		for(List<Route> currentRoute : currentRoutes)
		{
			double distance = 0.0;
			for(Route route : currentRoute)
			{
				distance += route.getDistance();
			}
			if(distance < bestDistance)
			{
				bestDistance = distance;
				bestRoute = currentRoute;
				int minutes =(int) Math.floor(distance);
				bestTime.plusMinutes(minutes + 5); // +5 Minutes for packaging
				distance -= minutes;
				int seconds = (int) Math.floor(distance * 60);
				bestTime.plusSeconds(seconds);
			}
		}
		//Route umkehren, falls Termine nicht eingehalten werden k�nnen
		if(!isRouteValid(bestRoute))
		{
			List<Route> tempRoute = bestRoute;
			for(int i = 0; i < bestRoute.size(); i++)
			{
				for (Route route : routes)
				{
					if(bestRoute.get(i).getDestinationOrderLocation().getAddress() == route.getOriginOrderLocation().getAddress() 
							&& bestRoute.get(i).getOriginOrderLocation().getAddress() == route.getDestinationOrderLocation().getAddress())
					{
						tempRoute.set(bestRoute.size() - 1 - i, route);
					}
				}
			}
			bestRoute = tempRoute;
		}
		//TODO Werte festhalten f�r Validierungen
		if (bestDistance > 50.0  || !isRouteValid(bestRoute))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private boolean isRouteValid(List<Route> bestRoute) {
		DateTime tmpTime = simTime.plusMinutes(5);
		if(tmpTime.isBefore(getLatestDeliveryStart(bestRoute)))
		{
			return true;
		}
		return false;
	}
	private DateTime getLatestDeliveryStart(List<Route> bestRoute) {
		DateTime latestDeliveryStart = new DateTime();
		DateTime deliveryTime = new DateTime(simTime.getYear(), simTime.getMonthOfYear(), simTime.getDayOfMonth(), simTime.getHourOfDay(), simTime.getMinuteOfHour(), simTime.getSecondOfMinute());
		for(Route bRoute : bestRoute)
		{
			DateTime dummy = new DateTime(simTime.getYear(), simTime.getMonthOfYear(), simTime.getDayOfMonth(), simTime.getHourOfDay(), simTime.getMinuteOfHour(), simTime.getSecondOfMinute());
			double distance = bRoute.getDistance();
			int minutes = (int) Math.floor(distance);
			deliveryTime.plusMinutes(minutes);
			distance -= minutes;
			int seconds = (int) Math.floor(distance * 60);
			deliveryTime.plusSeconds(seconds);
			dummy.plusHours(bRoute.getDestinationOrderDate().getHourOfDay() + 1 - deliveryTime.getHourOfDay());
			dummy.plusMinutes(bRoute.getDestinationOrderDate().getMinuteOfHour() - deliveryTime.getMinuteOfHour());
			dummy.plusSeconds(bRoute.getDestinationOrderDate().getSecondOfMinute() - deliveryTime.getSecondOfMinute());
			if(dummy.isBefore(latestDeliveryStart))
			{
				latestDeliveryStart = dummy;
			}
		}
		return latestDeliveryStart;
	}
	
	private LatLng getLatLng(String address) throws Exception {
		GeocodingResult[] results =  GeocodingApi.geocode(geoContext,
		    address + ",England").await();
		return results[0].geometry.location;	
	}
}

