package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;
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
	
	private GeoApiContext geoContext;
	private DateTime simTime;
	private Order[] incOrders;
	private int nextOrder;
	private Drohne[] drones;
	private Drohne activeDrone;
	private TestRepository testRepository;
	private TestService testService;
	private DateTime droneReturnTime;
	List<Route> bestRoute;
	double bestDistance;
	
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
		String [] input = new String[] { 
				"20.01.2017, 08:00, Strete, 2.1", 
				"20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7", 
				"20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7",
				"20.01.2017, 08:07, Strete, 3.2",
				"20.01.2017, 08:08, Churchstow, 1.6",
				"20.01.2017, 08:10, Hope, 2.0",
				"20.01.2017, 08:11, Malborough, 1.5",
				"20.01.2017, 08:11, Bigbury, 2.3"};
		this.incOrders = new Order[input.length];
		CreateOrderByList(input);
		this.nextOrder = 0;
		this.bestRoute = new ArrayList<Route>();
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
			this.incOrders[i] = new Order(orderDate, orderArr[2].trim(), weight);
		}
	}

	private void SetNextDroneActive() {
		int id;
		if (activeDrone == null || (id = activeDrone.getId()) == 5)
		{
			this.activeDrone = SetNextAvailableDroneActive(0);
		}
		else
		{
			this.activeDrone = SetNextAvailableDroneActive(id);
		}
		activeDrone.resetDrone();
		this.bestDistance = 0.0;
		this.bestRoute = null;
		this.droneReturnTime = null;
	}

	private Drohne SetNextAvailableDroneActive(int id) {
		boolean isAvailable = false;
		while(!isAvailable)
		{
			if(drones[id].getReturnTime() == null || drones[id].getReturnTime().isBefore(simTime))
			{
				return drones[id];
			}
			else
			{
				id++;
			}
		}
		return null; //Error display, that no drones are available
	}

	private void setSimTime() {
		simTime = incOrders[0].getOrderDate().minusMinutes(1);
	}

	private void Simulate() {
		while (nextOrder < incOrders.length) {
		AddMinute();
		AddOrder();
		CheckMaximumTime();
		}
		activeDrone.start(simTime);
	}

	private void AddMinute() {
		simTime = simTime.plusMinutes(1);
		if(droneReturnTime != null)
		{
			droneReturnTime.plusMinutes(1);
		}
	}
	
	private void AddOrder() {
		while(nextOrder < incOrders.length && simTime.equals(incOrders[nextOrder].getOrderDate())) 
		{
			if(incOrders[nextOrder].getWeight() > 4.0)
			{
				//TODO: Paket ist schwerer als erlaubt, Error display
			}
			if((activeDrone.getTotalPackageWeight() + incOrders[nextOrder].getWeight()) <= 4.0 && calcDroneRoutes(incOrders[nextOrder]))
			{
				addPackage(activeDrone, incOrders[nextOrder]);
			}
			// TODO: else Starte aktive Drohne und f�ge aktuelles Paket zu neuer Drohne hinzu
			else
			{
				activeDrone.start(simTime);
				SetNextDroneActive();
				calcDroneRoutes(incOrders[nextOrder]);
				addPackage(activeDrone, incOrders[nextOrder]);
			}	
			nextOrder++;
		}
	}

	private void addPackage(Drohne activeDrone, Order order) {
		activeDrone.addPackage(order);
		activeDrone.setTotalDistance(bestDistance);
		activeDrone.setReturnTime(droneReturnTime);
		activeDrone.setRoute(bestRoute);
	}

	private void CheckMaximumTime() {
		if(bestRoute != null)
		{
			if(!willAllDeliveriesBeInTime(simTime.plusMinutes(1), bestRoute))
			{
				activeDrone.start(simTime);
				SetNextDroneActive();
			}
		}
	}
	

	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private boolean calcDroneRoutes(Order currentOrder) {
		List<OrderLocation> orderLocations = new ArrayList<OrderLocation>();
		List<OrderLocation> currentOrderLocations = new ArrayList<OrderLocation>();
		List<Route> routes = new ArrayList<Route>();
		currentOrderLocations.add(new OrderLocation(0, "Salcombe", new LatLng(50.2375800, -3.7697910)));
		List<Order> orders = new ArrayList<Order>();
		for(Order o : activeDrone.getOrders())
		{
			orders.add(o);
		}
		List<OrderLocation> queryOrderLocations = new ArrayList<OrderLocation>();
		orders.add(currentOrder);
		for(Order o : orders)
		{
			orderLocations.add(new OrderLocation(o.getId(), o.getLocation()));
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
						if(o.getId() == currentOrderLocations.get(i).getOrderID())
						{
							originOrderDate = o.getOrderDate();
						}
					}
				}
				if(currentOrderLocations.get(j).getAddress() != "Salcombe")
				{
					for (Order o : orders)
					{
						if(o.getId() == currentOrderLocations.get(j).getOrderID())
						{
							destinationOrderDate = o.getOrderDate();
						}
					}
				}
				routes.add(new Route(currentOrderLocations.get(i), currentOrderLocations.get(j), destinationOrderDate , distance));
				routes.add(new Route(currentOrderLocations.get(j), currentOrderLocations.get(i), originOrderDate , distance));
				//CODE FOR TESTING
				/*int distanceKm = (int) distance;
				int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
				System.out.println("Distance from " + currentOrderLocations.get(i).getAddress() + " to " + currentOrderLocations.get(j).getAddress() + " : "
						+ distanceKm + "km " + distanceMeters + "m");*/
				//CODE FOR TESTING
			}	
		}
		//Beste Route ausw�hlen
		bestDistance = Double.MAX_VALUE;
		this.bestRoute = new ArrayList<Route>();
		List<ArrayList<Route>> currentRoutes = new ArrayList<ArrayList<Route>>();
		int pointer = 0;
		for(int i = 0; i< orders.size(); i++)
		{
			//System.out.println("i" + orders.get(i));
			List<Route> tempRoutes = new ArrayList<Route>();
			for (Route r : routes)
			{
				tempRoutes.add(r);
			}
			Iterator<Route> itr = tempRoutes.iterator();
			while(itr.hasNext())
			{
				Route route = itr.next();
				if(route.getOriginOrderLocation().getAddress() == "Salcombe" 
						&& route.getDestinationOrderLocation().getAddress() == orders.get(i).getLocation())
				{
					pointer = currentRoutes.size();
					if(orders.size() >= 3)
					{
						for (int j = 0; j < ((orders.size() -1) * (orders.size() - 2)); j++)
						{
							currentRoutes.add(new ArrayList<Route>(Arrays.asList(route)));
						}
					}
					else
					{
						currentRoutes.add(new ArrayList<Route>(Arrays.asList(route)));
					}
					itr.remove();
				}
				else if(route.getOriginOrderLocation().getAddress() == "Salcombe" 
						|| route.getDestinationOrderLocation().getAddress() == orders.get(i).getLocation())
				{
					itr.remove();
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
				tempOrders.remove(orders.get(i));
				for(int j = 0; j < tempOrders.size(); j++)
				{
					//System.out.println("j"+ orders.get(j));
					List<Route> temptempRoutes = new ArrayList<Route>();
					for (Route r : tempRoutes)
					{
						temptempRoutes.add(r);
					}
					itr = temptempRoutes.iterator();
					while(itr.hasNext())
					{
						Route route = itr.next();
						if(route.getOriginOrderLocation().getAddress() == orders.get(i).getLocation() 
								&& route.getDestinationOrderLocation().getAddress() == tempOrders.get(j).getLocation())
						{
							currentRoutes.get(pointer+j).add(route);
							if(orders.size() == 4)
							{
								currentRoutes.get(pointer+j+3).add(route);
							}
							itr.remove();
						}
						else if(route.getOriginOrderLocation().getAddress() == orders.get(i).getLocation() 
								|| route.getDestinationOrderLocation().getAddress() == tempOrders.get(j).getLocation())
						{
							itr.remove();
						}
					}
					if(orders.size() == 2)
					{
						currentRoutes.get(pointer+j).add(temptempRoutes.get(0));
					}
					else
					{
						List<Order> temptempOrders = new ArrayList<Order>();
						for (Order o : tempOrders)
						{
							temptempOrders.add(o);
						}
						temptempOrders.remove(tempOrders.get(j));
						for(int k = 0; k < temptempOrders.size(); k++)
						{
							//System.out.println("3rd" + temptempOrder);
							List<Route> temp3Routes = new ArrayList<Route>();
							for (Route r : temptempRoutes)
							{
								temp3Routes.add(r);
							}
							itr = temp3Routes.iterator();
							while(itr.hasNext())
							{
								Route route = itr.next();
								if(route.getOriginOrderLocation().getAddress() == tempOrders.get(j).getLocation() 
								&& route.getDestinationOrderLocation().getAddress() == temptempOrders.get(k).getLocation())
								{
									if(k == 0)
									{
										currentRoutes.get(pointer+j).add(route);
									}
									else
									{
										currentRoutes.get(pointer+j+3).add(route);
									}
									itr.remove();
								}
								else if(route.getOriginOrderLocation().getAddress() == tempOrders.get(j).getLocation() 
										|| route.getDestinationOrderLocation().getAddress() == temptempOrders.get(k).getLocation())
								{
									itr.remove();
								}
							}
							if(orders.size() == 3)
							{
								currentRoutes.get(pointer+j+k).add(temp3Routes.get(0));
							}
							else
							{
								List<Order> temp3Orders = new ArrayList<Order>();
								for (Order o : temptempOrders)
								{
									temp3Orders.add(o);
								}
								temp3Orders.remove(temptempOrders.get(k));
								for(int m = pointer; m < currentRoutes.size(); m++)
								{
									//System.out.println("4th" + temp3Orders.toString());
									List<Route> temp4Routes = new ArrayList<Route>();
									for (Route r : temp3Routes)
									{
										temp4Routes.add(r);
									}
									itr = temp4Routes.iterator();
									while(itr.hasNext())
									{
										Route route = itr.next();
										if(route.getOriginOrderLocation().getAddress() == currentRoutes.get(m).get(2).getDestinationOrderLocation().getAddress()
												&& route.getDestinationOrderLocation().getAddress() == temp3Orders.get(0).getLocation())
										{
											currentRoutes.get(m).add(route);
											itr.remove();
										}
										else if(route.getOriginOrderLocation().getAddress() == currentRoutes.get(m).get(2).getDestinationOrderLocation().getAddress()
												|| route.getDestinationOrderLocation().getAddress() == temp3Orders.get(0).getLocation())
										{
											itr.remove();
										}
									}
									currentRoutes.get(m).add(temp4Routes.get(0));
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
			}
		}
		//Route umkehren, falls Termine nicht eingehalten werden k�nnen
		if(!willAllDeliveriesBeInTime(simTime, bestRoute))
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
		if (bestDistance > 50.0  || !willAllDeliveriesBeInTime(simTime, bestRoute))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	private boolean willAllDeliveriesBeInTime(DateTime time, List<Route> bestRoute) {
		DateTime deliveryTime = new DateTime(time.getYear(), time.getMonthOfYear(), time.getDayOfMonth(),
				time.getHourOfDay(), time.getMinuteOfHour() + 5, time.getSecondOfMinute());
		for(Route bRoute : bestRoute)
		{
			double distance = bRoute.getDistance();
			int minutes = (int) Math.floor(distance);
			deliveryTime = deliveryTime.plusMinutes(minutes);
			distance -= minutes;
			int seconds = (int) Math.floor(distance * 60);
			deliveryTime = deliveryTime.plusSeconds(seconds);
			if(bRoute.getDestinationOrderDate() != null && !deliveryTime.isBefore(bRoute.getDestinationOrderDate().plusHours(1)))
					{
						return false;
					}
		}
		droneReturnTime = deliveryTime;
		return true;
	}
	
	private LatLng getLatLng(String address) throws Exception {
		GeocodingResult[] results =  GeocodingApi.geocode(geoContext,
		    address + ",England").await();
		return results[0].geometry.location;	
	}
}

