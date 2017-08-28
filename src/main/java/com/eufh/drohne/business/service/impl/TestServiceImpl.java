package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
	Calendar simTime;
	Order[] order;
	List<Coordinates> locCoords;
	int nextOrder;
	Drohne[] drones;
	Drohne activeDrone;
	List<String> currentAddresses;

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
	
	/*@Override
	@Transactional
	public void saveCoordinates(Coordinates coordinates) {
		testRepository.saveCoordinates(coordinates);
	}*/

	/*
	 * Entry point for the drone simulation
	 */
	public void startDroneSimulation() {
		//TODO PHKO: expand method as needed (DoSomething)
		String [] input = new String[] { "20.01.2017, 08:00, Strete, 2.1", "20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7", "20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7" };
		order = new Order[input.length];
		CreateOrderByList(input);
		/*order = new String[input.length][4];
		for(int i = 0; i < input.length; i++) {
			order[i] = input[i].split(",");
		}*/
		
		locCoords = new ArrayList<Coordinates>();
		currentAddresses = new ArrayList<String>();
		nextOrder = 0;
		drones = new Drohne[] {new Drohne(), new Drohne(), new Drohne(), new Drohne(), new Drohne()};
		SetNextDroneActive();
		setSimTime();
		
		
		Simulate();
		
	}

	private void CreateOrderByList(String[] input) { 
		for(int i = 0; i < input.length; i++) {
			String[] orderArr = input[i].split(",");
			String[] date = orderArr[0].trim().split("\\.");
			String[] time = orderArr[1].trim().split(":");
			Calendar orderDate = new GregorianCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]), 
					Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));
			double weight = Double.parseDouble(orderArr[3].trim());
			order[i] = new Order(orderDate, orderArr[2].trim(), weight);
		}
	}

	private void SetNextDroneActive() {
		if (activeDrone == null || activeDrone.getId() == 5)
		{
			activeDrone = drones[0];
		}
		else
		{
			activeDrone = drones[activeDrone.getId()];
		}
		activeDrone.resetDrone();
		currentAddresses.clear();
		currentAddresses.add("Salcombe");
	}

	private void setSimTime() {
		simTime = order[0].getOrderDate();
		simTime.add(Calendar.MINUTE, -1);
	}

	private void Simulate() {
		AddMinute();
		AddOrder();
		CheckMaximumTime();
	}

	private void AddMinute() {
		simTime.add(Calendar.MINUTE, 1);
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
				calcDroneRoutes(order[nextOrder].getLocation());
				
			}
			// TODO: else Starte aktive Drohne und füge aktuelles Paket zu neuer Drohne hinzu
			else
			{
				StartActiveDrone();
				calcDroneRoutes(order[nextOrder].getLocation());
				activeDrone.addPackage(order[nextOrder].getWeight());
			}
			
			nextOrder++;
		}
	}
	
	private void StartActiveDrone() {
		// TODO Auto-generated method stub
		
	}

	private void CheckMaximumTime() {
		// TODO Auto-generated method stub
		
	}
	

	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private void calcDroneRoutes (String address) {
		currentAddresses.add(address);
		List<Coordinates> locCoords = new ArrayList<Coordinates>();
		locCoords.add(new Coordinates(currentAddresses.get(0), 50.2375800, -3.7697910)); //Salcombe
		//TODO: Get known LatLngs from DB
		geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
		try {
			//TODO: Initialize mit 0, sobald die bekannten LatLngs in der DB sind
			for (int i = 1; i < currentAddresses.size(); i++)
			{
				LatLng latLng = getLatLng(currentAddresses.get(i));
				locCoords.add(new Coordinates(currentAddresses.get(i), latLng.lat, latLng.lng));	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally 
		{
			for (int i = 0; i < locCoords.size(); i++) 
			{
				for (int j = i + 1; j < locCoords.size(); j++) 
				{
					double distance = Haversine.getDistance(locCoords.get(i).getLatitude(),locCoords.get(i).getLongitude(),
							locCoords.get(j).getLatitude(), locCoords.get(j).getLongitude());
					//CODE FOR TESTING
					int distanceKm = (int) distance;
					int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
					System.out.println("Distance from " + locCoords.get(i).getLocation() + " to " + locCoords.get(j).getLocation() + " : "
							+ distanceKm + "km " + distanceMeters + "m");
					//CODE FOR TESTING
				}
			}
		}
	}
	
	private LatLng getLatLng(String address) throws Exception {
		GeocodingResult[] results =  GeocodingApi.geocode(geoContext,
		    address + ",England").await();
		return results[0].geometry.location;	
	}
}

