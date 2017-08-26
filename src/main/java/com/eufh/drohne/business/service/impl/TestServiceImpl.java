package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.repository.TestRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class TestServiceImpl implements TestService {
	
	GeoApiContext geoContext;
	Calendar simTime;
	String[][] order;
	int nextOrder;
	Drohne[] drones;
	int activeDroneId;
	Drohne activeDrone;
	List<String> currentAddresses;

	private TestRepository testRepository;

	public TestServiceImpl(TestRepository repo) {
		this.testRepository = repo;
	}

	@Override
	public ArrayList<Drohne> findAll() {
		return testRepository.findAll();
	}

	@Override
	public Drohne findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Drohne> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drohne findByPers(String pers) {
		return testRepository.findByPers(pers);
	}

	@Override
	@Transactional
	public Drohne save(Drohne drohne) {
		return testRepository.save(drohne);
	}

	/*
	 * Entry point for the drone simulation
	 */
	public void startDroneSimulation() {
		//TODO PHKO: expand method as needed (DoSomething)
		String [] input = new String[] { "20.01.2017, 08:00, Strete, 2.1", "20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7", "20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7" };
		for(int i = 0; i < input.length; i++) {
			order[i] = input[i].split(";");
		}
		
		drones = new Drohne[5];
		activeDroneId = 0;
		NewDrone();
		
		setSimTime();
		nextOrder = 0;
		currentAddresses = new ArrayList<String>();
		
		Simulate();
		
	}
	
	private void NewDrone() {
		activeDrone = drones[0];
		activeDroneId++;
		activeDrone.resetDrone();
		currentAddresses.clear();
		currentAddresses.add("Salcombe");
	}

	private void setSimTime() {
		String[] date = order[0][0].split(".");
		String[] time = order[0][1].split(":");
		simTime.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]),
				Integer.parseInt(time[0]), Integer.parseInt(time[1]));
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
		String[] date = order[nextOrder][0].split(".");
		String[] time = order[nextOrder][1].split(":");
		if(simTime.get(Calendar.YEAR) == Integer.parseInt(date[2]) 
				&& simTime.get(Calendar.MONTH) == Integer.parseInt(date[1])
				&& simTime.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(date[0])
				&& simTime.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(time[0])
				&& simTime.get(Calendar.MINUTE) == Integer.parseInt(time[1])) 
		{
			if((Double.parseDouble(order[nextOrder][3])) > 4.0)
			{
				//Paket ist schwerer als erlaubt, Error display
			}
			if((activeDrone.getTotalPackageWeight() + (Double.parseDouble(order[nextOrder][3])) <= 4.0))
			{
				calcDroneRoutes(order[nextOrder][2]);
				
			}
			// else Starte aktive Drohne und füge aktuelles Paket zu neuer Drohne hinzu
			else
			{
				StartActiveDrone();
				calcDroneRoutes(order[nextOrder][2]);
				activeDrone.addPackage(Double.parseDouble(order[nextOrder][3]));
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
		List<LatLng> locLatLngs = new ArrayList<LatLng>();
		locLatLngs.add(new LatLng(50.2375800, -3.7697910)); //LatLng for Salcombe
		//TODO: Get known LatLngs from DB
		geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
		try {
			for (String loc : currentAddresses)
				locLatLngs.add(getLatLng(loc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally 
		{
			for (int i = 0; i < locLatLngs.size(); i++) 
			{
				for (int j = i + 1; j < locLatLngs.size(); j++) 
				{
					if (i != j) 
					{
						double distance = Haversine.getDistance(locLatLngs.get(i).lat,locLatLngs.get(i).lng,
								locLatLngs.get(j).lat, locLatLngs.get(j).lng);
						//CODE FOR TESTING
						int distanceKm = (int) distance;
						int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
						System.out.println("Distance from " + currentAddresses.get(i) + " to " + currentAddresses.get(j) + " : "
								+ distanceKm + "km " + distanceMeters + "m");
						//CODE FOR TESTING
					}
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

