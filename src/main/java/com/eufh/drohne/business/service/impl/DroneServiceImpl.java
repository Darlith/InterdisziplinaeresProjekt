package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.repository.DroneRepository;

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
	public Drohne findOne(Integer id) {
		return droneRepository.findOne(id);
	}

	@Override
	@Transactional
	public Drohne save(Drohne drohne) {
		return droneRepository.save(drohne);
	}

	public void deleteOne(Integer id) {
		droneRepository.delete(id);
	}
}

