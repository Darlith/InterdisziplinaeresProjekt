package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;
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
		calcDroneRoutes();
	}
	
	//TODO PHKO: Weiterentwickeln, Schnittstelle zum Frontend und Datenbank bilden
	private void calcDroneRoutes () {
		
		String baseLoc = "Salcombe";
		List<LatLng> locLatLngs = new ArrayList<LatLng>();
		double baseLat = 50.2375800;
		double baseLong = -3.7697910;
		String[] inputArr = new String[] { "20.01.2017, 08:00, Strete, 2.1", "20.01.2017, 08:01, Thurlestone, 1.2",
				"20.01.2017, 08:02, Beesands, 0.7", "20.01.2017, 08:02, West Charleton, 3.9",
				"20.01.2017, 08:05, Kingsbridge, 2.7" };
		List<String> addresses = new ArrayList<String>();
		geoContext = new GeoApiContext.Builder().apiKey("AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM").build();
		for (String input : inputArr)
			addresses.add(input.split(",")[2]);

		try {
			for (String address : addresses)
				locLatLngs.add(getLatLng(address));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			for (int i = 0; i < locLatLngs.size(); i++) {
				double distance = Haversine.getDistance(baseLat, baseLong, locLatLngs.get(i).lat,
						locLatLngs.get(i).lng);
				int distanceKm = (int) distance;
				int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
				System.out.println("Distance from " + baseLoc + " to " + addresses.get(i) + " : " + distanceKm + "km "
						+ distanceMeters + "m");
			}
			for (int i = 0; i < locLatLngs.size(); i++) {
				for (int j = i + 1; j < locLatLngs.size(); j++) {
					if (i != j) {
						double distance = Haversine.getDistance(locLatLngs.get(i).lat,locLatLngs.get(i).lng,
								locLatLngs.get(j).lat, locLatLngs.get(j).lng);
						int distanceKm = (int) distance;
						int distanceMeters = (int) ((distance - (double) distanceKm) * 1000);
						System.out.println("Distance from " + addresses.get(i) + " to " + addresses.get(j) + " : "
								+ distanceKm + "km " + distanceMeters + "m");
					}
				}
			}
		}
	}
	private LatLng getLatLng(String address) throws Exception {
		GeocodingResult[] results =  GeocodingApi.geocode(geoContext,
		    address + ",England").await();
		return results[0].geometry.location;
		
		
		/*int responseCode = 0;
		String api = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + address
				+ ",England&key=AIzaSyDMPJ3sP0kzCvOtV2PPxUfgL0axoQff-mM";
		URL url = new URL(api);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.connect();
		responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			;
			Document document = builder.parse(conn.getInputStream());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/GeocodeResponse/status");
			String status = (String) expr.evaluate(document, XPathConstants.STRING);
			if (status.equals("OK")) {
				expr = xpath.compile("//geometry/location/lat");
				String latitude = (String) expr.evaluate(document, XPathConstants.STRING);
				expr = xpath.compile("//geometry/location/lng");
				String longitude = (String) expr.evaluate(document, XPathConstants.STRING);
				return new String[] { latitude, longitude };
			} else {
				throw new Exception("Error from the API - response status: " + status);
			}
		}
		return null;*/
	}
}

