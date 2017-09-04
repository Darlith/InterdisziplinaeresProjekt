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
	List<Coordinates> locCoords;
	int nextOrder;
	Drohne[] drones;
	Drohne activeDrone;

	private TestRepository testRepository;

	public TestServiceImpl(TestRepository repo) {
		this.testRepository = repo;
	}

	@Override
	public ArrayList<Order> findAll() {
		return testRepository.findAll();
	}

	@Override
	public Order findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Order save(Order order) {
		return testRepository.save(order);
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
		
		this.locCoords = new ArrayList<Coordinates>();
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
				if(calcDroneRoutes())
				{
					activeDrone.addPackage(order[nextOrder]);
				}
				
				
			}
			// TODO: else Starte aktive Drohne und f�ge aktuelles Paket zu neuer Drohne hinzu
			else
			{
				activeDrone.start(simTime);
				SetNextDroneActive();
				activeDrone.addPackage(order[nextOrder]);
				calcDroneRoutes();
			}
			
		}
		nextOrder++;
	}

	private void CheckMaximumTime() {
		// TODO Auto-generated method stub
		
	}
	

	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private boolean calcDroneRoutes() {
		List<OrderLocation> currentOrderLocations = new ArrayList<OrderLocation>();
		List<Route> routes = new ArrayList<Route>();
		currentOrderLocations.add(new OrderLocation(0, "Salcombe", new LatLng(50.2375800, -3.7697910)));
		List<Order> orders = activeDrone.getOrders();
		for(Order order : orders)
		{
			currentOrderLocations.add(new OrderLocation(order.getId(), order.getLocation()));
		}
		//TODO: Get known LatLngs from DB
		geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
		try {
			//TODO: Initialize mit 0, sobald die bekannten LatLngs in der DB sind
			for (int i = 1; i < currentOrderLocations.size(); i++)
			{
				LatLng latLng = getLatLng(currentOrderLocations.get(i).getAddress());
				currentOrderLocations.get(i).setLatlng(latLng);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < currentOrderLocations.size(); i++) 
		{
			for (int j = i + 1; j < currentOrderLocations.size(); j++) 
			{
				double distance = Haversine.getDistance(locCoords.get(i).getLatitude(),locCoords.get(i).getLongitude(),
						locCoords.get(j).getLatitude(), locCoords.get(j).getLongitude());
				DateTime originOrderDate = null;
				DateTime destinationOrderDate = null;
				if(currentOrderLocations.get(i).getAddress() != "Salcombe")
				{			
					int k = 0;
					while (originOrderDate == null)
					{
						originOrderDate = orders.get(k).getOrderDateById(currentOrderLocations.get(i).getOrderID());
						k++;
					}
				}
				if(currentOrderLocations.get(j).getAddress() != "Salcombe")
				{
					int l = 0;
					while(destinationOrderDate == null)
					{
						destinationOrderDate = orders.get(l).getOrderDateById(currentOrderLocations.get(j).getOrderID());
						l++;
					}
				}
				routes.add(new Route(currentOrderLocations.get(i), currentOrderLocations.get(j), destinationOrderDate , distance));
				routes.add(new Route(currentOrderLocations.get(j), currentOrderLocations.get(i), originOrderDate , distance));
				//CODE FOR TESTING
				int distanceKm = (int) distance;
				int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
				System.out.println("Distance from " + locCoords.get(i).getLocation() + " to " + locCoords.get(j).getLocation() + " : "
						+ distanceKm + "km " + distanceMeters + "m");
				//CODE FOR TESTING
			}
		}
		//Beste Route ausw�hlen
		double bestDistance = Double.MAX_VALUE;
		DateTime bestTime = new DateTime();
		List<Route> bestRoute = new ArrayList<Route>();
		List<ArrayList<Route>> currentRoutes = new ArrayList<ArrayList<Route>>();
		for(Order order : orders)
		{
			List<Route> tempRoutes = routes;
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
				List<Order> tempOrders = orders;
				tempOrders.remove(order);
				for(int i = 0; i < tempOrders.size(); i++)
				{
					List<Route> temptempRoutes = tempRoutes;
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
						List<Order> temptempOrders = tempOrders;
						temptempOrders.remove(tempOrders.get(i));
						for(Order temptempOrder : temptempOrders)
						{
							List<Route> temp3Routes = temptempRoutes;
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
								List<Order> temp3Orders = temptempOrders;
								temp3Orders.remove(temptempOrder);
								for(int j = 0; j < currentRoutes.size(); j++)
								{
									List<Route> temp4Routes = temp3Routes;
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
				bestTime.plusMinutes(((int) distance) + 5); // +5 Minutes for packaging
				distance -= (int) distance;
				bestTime.plusSeconds((int)(distance * 60));
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
		DateTime deliveryTime = new DateTime();
		for(Route bRoute : bestRoute)
		{
			DateTime dummy = new DateTime();
			double distance = bRoute.getDistance();
			deliveryTime.plusMinutes((int) distance);
			distance -= (int) distance;
			deliveryTime.plusSeconds((int)(distance * 60));
			dummy.plusHours(bRoute.getDestinationOrderDate().getHourOfDay() - deliveryTime.getHourOfDay());
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

