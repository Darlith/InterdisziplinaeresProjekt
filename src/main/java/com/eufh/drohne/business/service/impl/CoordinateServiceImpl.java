package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;

import com.eufh.drohne.domain.*;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.CoordinateService;
import com.eufh.drohne.repository.CoordinateRepository;

public class CoordinateServiceImpl implements CoordinateService {
	
	private CoordinateRepository coordinateRepository;
	
	public CoordinateServiceImpl(CoordinateRepository repo) {
		this.coordinateRepository = repo;
	}

	@Override
	public ArrayList<Coordinates> findAll() {
		return coordinateRepository.findAll();
	}

	@Override
	public Coordinates findOne(String id) {
		return coordinateRepository.findOne(id);
	}

	@Override
	@Transactional
	public Coordinates save(Coordinates coordinates) {
		return coordinateRepository.save(coordinates);
	}

}