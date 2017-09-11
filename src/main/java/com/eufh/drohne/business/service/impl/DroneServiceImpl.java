package com.eufh.drohne.business.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;
import com.eufh.drohne.repository.DroneRepository;
import com.eufh.drohne.repository.TestRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class DroneServiceImpl implements DroneService {
	
	private DroneRepository droneRepository;

	public DroneServiceImpl(DroneRepository repo) {
		this.droneRepository = repo;
	}

	@Override
	public ArrayList<Drohne> findAll() {
		return droneRepository.findAll();
	}

	@Override
	@Transactional
	public Drohne save(Drohne drohne) {
		return droneRepository.save(drohne);
	}
}

