package com.eufh.drohne.business.service.impl;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.eufh.drohne.domain.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.repository.DroneRepository;
import com.eufh.drohne.repository.TestRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class TestServiceImpl implements TestService {
	
	private GeoApiContext geoContext;
	private DateTime simTime;
	private List<Order> incOrders;
	private Drohne[] drones;
	private Drohne activeDrone;
	private TestRepository testRepository;
	private TestService testService;
	private DroneService droneService;
	private ProcessedOrderService processedOrderService;
	private DateTime droneReturnTime;
	private List<Route> droneRoute;
	private double droneDistance;
	private DateTime nextDroneAvailableTime;
	private int nextDroneAvailableId;
	private int dronePointer;
	private Iterator<Order> orderIterator;
	
	public TestServiceImpl(TestRepository repo, TestService testService, DroneService droneService,
			ProcessedOrderService processedOrderService) {
		this.testRepository = repo;
		this.testService = testService;
		this.droneService = droneService;
		this.processedOrderService = processedOrderService;
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

	public void startWithCsvOrders(CsvOrder[] orders) {
		String[] input = new String[orders.length];
		for(int i = 0; i<orders.length; i++) {
			input[i] = orders[i].toString();
		}

		this.startDroneSimulation(input);
	}

	public void startWithDefaultOrders() {
		String [] defaultOrders = new String[] {
				"20.01.2017, 08:00, Strete, 2.1",
				"20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7",
				"20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7",
				"20.01.2017, 08:07, Strete, 3.2",
				"20.01.2017, 08:08, Churchstow, 1.6",
				"20.01.2017, 08:10, Hope Cove, 2.0",
				"20.01.2017, 08:11, Malborough, 1.5",
				"20.01.2017, 08:11, Bigbury, 2.3",
				"20.01.2017, 08:14, Thurlestone, 1.2",
				"20.01.2017, 08:20, Kellaton, 3.1",
				"20.01.2017, 08:24, Aveton Gifford, 2.7",
				"20.01.2017, 08:28, Sherford, 1.3",
				"20.01.2017, 08:30, East Prawle, 2.0",
				"20.01.2017, 08:31, Strete, 1.2",
				"20.01.2017, 08:32, South Milton, 0.9",
				"20.01.2017, 08:34, Malborough, 1.2",
				"20.01.2017, 08:35, West Charleton, 1.4",
				"20.01.2017, 08:35, Hope Cove, 1.8",
				"20.01.2017, 08:37, Sherford, 3.8",
				"20.01.2017, 08:38, Aveton Gifford, 3.6",
				"20.01.2017, 08:39, Strete, 2.9",
				"20.01.2017, 08:41, Beesands, 2.5",
				"20.01.2017, 08:42, East Prawle, 1.7",
				"20.01.2017, 08:42, Kingsbridge, 2.7",
				"20.01.2017, 08:43, Strete, 3.2",
				"20.01.2017, 08:43, Churchstow, 1.6",
				"20.01.2017, 08:44, Hope Cove, 2.7",
				"20.01.2017, 08:44, Malborough, 1.4",
				"20.01.2017, 08:45, Bigbury, 1.3",
				"20.01.2017, 08:45, Thurlestone, 3.2",
				"20.01.2017, 08:46, Kellaton, 3.3",
				"20.01.2017, 08:48, Aveton Gifford, 1.7",
				"20.01.2017, 08:50, Sherford, 0.3",
				"20.01.2017, 08:51, East Prawle, 1.0",
				"20.01.2017, 08:51, Sherford, 1.3",
				"20.01.2017, 08:53, East Prawle, 2.1",
				"20.01.2017, 08:54, Strete, 3.2",
				"20.01.2017, 08:56, South Milton, 0.5",
				"20.01.2017, 08:58, Malborough, 1.4",
				"20.01.2017, 08:59, West Charleton, 2.4",
				"20.01.2017, 08:59, Hope Cove, 0.8"
		};
		this.startDroneSimulation(defaultOrders);
	}

	/*
	 * Entry point for the drone simulation
	 */
	public void startDroneSimulation(String[] input) {
		//TODO PHKO: expand method as needed (DoSomething)
		this.incOrders = new ArrayList<Order>();
		CreateOrderByList(input);
		this.drones = this.getDrones();
		//this.drones = new Drohne[] { new Drohne(), new Drohne(), new Drohne(), new Drohne(), new Drohne() };
		//for(Drohne d: drones) {
		//	droneService.save(d);
		//}
		this.dronePointer = drones[0].getId() - 1;
		SetNextDroneActive();
		setSimTime();
		
		
		Simulate();
	}

	private Drohne[] getDrones() {
		ArrayList<Drohne> allDrones = this.droneService.findAll();
		Drohne[] arrayDrones = new Drohne[allDrones.size()];
		for(Drohne d: allDrones) {
			d.resetDrone();
		}
		if (arrayDrones.length > 0) {
			return allDrones.toArray(arrayDrones);
		}
		// Initialize default drones
		int defaultDroneAmount = 5;
		Drohne[] drones = new Drohne[5];
		for (int i = 0; i < drones.length; i++) {
			Drohne d = new Drohne();
			this.droneService.save(d);
			drones[i] = d;
		}

		return drones;
	}
	
	private void Simulate() {
		while (incOrders.size() > 0) {
		AddMinute();
		AddOrder();
		CheckMaximumTime();
		}
		if(activeDrone != null)
		{
			startDrone();
		}
		//TEST TODO PHKO: REMOVE
		System.out.println("========================================================================================");
		System.out.println("========================================================================================");
		//TEST
	}

	private void startDrone() {
		List<ProcessedOrder> poList = activeDrone.start(simTime);
		for(ProcessedOrder po : poList)
		{
			processedOrderService.save(po);
		}
		droneService.save(activeDrone);
	}

	private void CreateOrderByList(String[] input) { 
		for(int i = 0; i < input.length; i++) {
			String[] orderArr = input[i].split(",");
			String[] date = orderArr[0].trim().split("\\.");
			String[] time = orderArr[1].trim().split(":");
			DateTime orderDate = new DateTime(Integer.parseInt(date[2]), Integer.parseInt(date[1]), 
					Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
			double weight = Double.parseDouble(orderArr[3].trim());
			this.incOrders.add(new Order(orderDate, orderArr[2].trim(), weight));
		}
	}

	private void SetNextDroneActive() {
		int id;
		if (activeDrone == null || (id = activeDrone.getId()) == (dronePointer + drones.length))
		{
			this.activeDrone = SetNextAvailableDroneActive(dronePointer);
		}
		else
		{
			this.activeDrone = SetNextAvailableDroneActive(id);
		}
		if(activeDrone != null)
		{
			activeDrone.resetDrone();
		}
		this.droneDistance = 0.0;
		this.droneRoute = new ArrayList<Route>();
		this.droneReturnTime = null;
	}

	private Drohne SetNextAvailableDroneActive(int id) {
		id -= dronePointer;
		this.nextDroneAvailableTime = drones[id].getReturnTime();
		this.nextDroneAvailableId = drones[id].getId();
		boolean isAvailable = false;
		int repetition = 0;
		while(!isAvailable && repetition <= drones.length)
		{
			if(drones[id].getReturnTime() == null 
					|| drones[id].getReturnTime().isBefore(simTime) || drones[id].getReturnTime().isEqual(simTime))
			{
				return drones[id];
			}
			else
			{
				if(drones[id].getReturnTime().isBefore(nextDroneAvailableTime))
				{
					nextDroneAvailableTime = drones[id].getReturnTime();
					nextDroneAvailableId = drones[id].getId();
				}
				if(id == (drones.length - 1))
				{
					id = 0;
				}
				else
				{
				id++;
				}
			}
			repetition++;
		}
		return null; //if no drones are available
	}

	private void setSimTime() {
		simTime = incOrders.get(0).getOrderDate().minusMinutes(1);
	}

	private void AddMinute() {
		simTime = simTime.plusMinutes(1);
		if(droneReturnTime != null)
		{
			droneReturnTime = droneReturnTime.plusMinutes(1);
			if(activeDrone != null && activeDrone.getReturnTime() != null)
			{
			activeDrone.setReturnTime(activeDrone.getReturnTime().plusMinutes(1));
			}
		}
		if (activeDrone == null)
		{
			if(!simTime.isBefore(nextDroneAvailableTime))
			{
				activeDrone = droneService.findOne(nextDroneAvailableId);
				activeDrone.resetDrone();
			}
		}
	}
	
	private void AddOrder() {
		boolean checkNextDrone = true;		
		while(activeDrone != null && checkNextDrone)
		{
			checkNextDrone = false;
			boolean startDrone = false;
			orderIterator = incOrders.iterator();
			while(orderIterator.hasNext())
			{
				Order order = orderIterator.next();
				if (simTime.isEqual(order.getOrderDate()) 
						|| simTime.isAfter(order.getOrderDate()))
				{
					int calcResult  = 0;
					if(order.getWeight() > 4.0)
					{
						//TODO: Paket ist schwerer als erlaubt, Error display
						orderIterator.remove();
					}
					//Calculation may provide 3 states : success, failure or error. (1, 0, -1)
					else if((activeDrone.getTotalPackageWeight() + order.getWeight()) <= 4.0 
							&& (calcResult = calcDroneRoutes(order)) == 1)
					{
						addPackage(activeDrone, order);
						orderIterator.remove();
					}
					// TODO: else Starte aktive Drohne und f�ge aktuelles Paket zu neuer Drohne hinzu
					else if(calcResult == -1)
					{
						orderIterator.remove();
						//No code will be executed on if illegal adresses are input and the order will be ignored.
					}
					else
					{
						startDrone = true;
					}	
				}
			}
			if(startDrone || activeDrone.getTotalPackageWeight() >= 3.5 || this.droneDistance >= 45.0)
			{
				startDrone();
				SetNextDroneActive();
				checkNextDrone = true;
			}
		}
	}

	private void addPackage(Drohne activeDrone, Order order) {
		activeDrone.addPackage(order);
		activeDrone.setTotalDistance(droneDistance);
		activeDrone.setReturnTime(droneReturnTime);
		activeDrone.setRoute(droneRoute);
	}

	private void CheckMaximumTime() {
		if(droneRoute != null)
		{
			if(!willAllDeliveriesBeInTime(simTime.plusMinutes(1)))
			{
				startDrone();
				SetNextDroneActive();
			}
		}
	}
	

	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private int calcDroneRoutes(Order currentOrder) {
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
			if(geoContext == null)
				geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
			try {
				//TODO: Initialize mit 0, sobald die bekannten LatLngs in der DB sind
				for (int i = 0; i < queryOrderLocations.size(); i++)
				{
					OrderLocation o = queryOrderLocations.get(i);
					LatLng latLng = getLatLng(o.getAddress());
					o.setLatlng(latLng);
					double distance = Haversine.getDistance(currentOrderLocations.get(0).getLatlng().lat,currentOrderLocations.get(0).getLatlng().lng,
							latLng.lat, latLng.lng);
					if( distance > 25.0)
					{
						throw new IllegalArgumentException(o.getAddress());
					}
					currentOrderLocations.add(o);
					Coordinates c = new Coordinates(o.getAddress(), o.getLatlng().lat, o.getLatlng().lng);
					testService.save(c);
				}
			}
			catch (IllegalArgumentException ie)
			{
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println("Der Bestellort \"" + ie.getMessage() + "\" ist nicht innerhalb des 25km Lieferradius und wir daher nicht ausgeliefert.");
				System.out.println("-------------------------------------------------------------------------------");
				return -1;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < currentOrderLocations.size(); i++) 
		{
			for (int j = i + 1; j < currentOrderLocations.size(); j++) 
			{
				double distance = Haversine.getDistance(currentOrderLocations.get(i).getLatlng().lat,currentOrderLocations.get(i).getLatlng().lng,
						currentOrderLocations.get(j).getLatlng().lat, currentOrderLocations.get(j).getLatlng().lng);
				if (distance > 50.0)
				{
					throw new IndexOutOfBoundsException();
				}
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
		double bestDistance = Double.MAX_VALUE;
		List<Route> bestRoute = new ArrayList<Route>();
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
		if (bestDistance > 50.0)  //|| !willAllDeliveriesBeInTime(simTime, bestRoute))
		{
			return 0;
		}
		this.droneDistance = bestDistance;
		if(!droneRoute.isEmpty())
		{
			droneRoute.clear();
		}
		for(Route br : bestRoute)
		{
			droneRoute.add(br);
		}
		//calc deliveryTime and reverse the route if less deliveries will be delayed this way.
		reverseRouteIfNecessary(routes);
		return 1;
	}
	private void reverseRouteIfNecessary(List<Route> routes)
	{
		DateTime deliveryTime = simTime.plusMinutes(5);
		int deliveriesNotInTime = 0;
		
		for(Route bRoute : droneRoute)
		{
			double distance = bRoute.getDistance();
			int minutes = (int) Math.floor(distance);
			deliveryTime = deliveryTime.plusMinutes(minutes);
			distance -= minutes;
			int seconds = (int) Math.floor(distance * 60);
			deliveryTime = deliveryTime.plusSeconds(seconds);
			if(bRoute.getDestinationOrderDate() != null && !deliveryTime.isBefore(bRoute.getDestinationOrderDate().plusHours(1)))
					{
						deliveriesNotInTime++;
					}
		}
		if(deliveriesNotInTime > 0)
		{
			int tempNotInTime = 0;
			DateTime tempTime = simTime.plusMinutes(5);
			List<Route> tempRoute = new ArrayList<Route>();
			for(Route b: droneRoute)
			{
				tempRoute.add(b);
			}
			for(int i = 0; i < droneRoute.size(); i++)
			{
				for (Route route : routes)
				{
					if(droneRoute.get(i).getDestinationOrderLocation().getAddress() == route.getOriginOrderLocation().getAddress() 
							&& droneRoute.get(i).getOriginOrderLocation().getAddress() == route.getDestinationOrderLocation().getAddress())
					{
						tempRoute.set(droneRoute.size() - 1 - i, route);
					}
				}
			}
			for(Route bRoute : tempRoute)
			{
				double distance = bRoute.getDistance();
				int minutes = (int) Math.floor(distance);
				tempTime = tempTime.plusMinutes(minutes);
				distance -= minutes;
				int seconds = (int) Math.floor(distance * 60);
				tempTime = tempTime.plusSeconds(seconds);
				if(bRoute.getDestinationOrderDate() != null && !tempTime.isBefore(bRoute.getDestinationOrderDate().plusHours(1)))
						{
							tempNotInTime++;
						}
			}
			if(tempNotInTime < deliveriesNotInTime)
			{
				droneRoute = tempRoute;
			}
		}
		droneReturnTime = deliveryTime;
	}
	
	private boolean willAllDeliveriesBeInTime(DateTime time) {
		DateTime deliveryTime = time.plusMinutes(5);
		
		for(Route bRoute : this.droneRoute)
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
		this.droneReturnTime = deliveryTime;
		return true;
	}
	
	private LatLng getLatLng(String address) throws Exception {
		GeocodingResult[] results =  GeocodingApi.geocode(geoContext,
		    address + ",England").await();
		return results[0].geometry.location;	
	}
}

