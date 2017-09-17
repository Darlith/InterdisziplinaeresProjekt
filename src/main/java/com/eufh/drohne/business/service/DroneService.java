package com.eufh.drohne.business.service;

import java.util.ArrayList;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;

public interface DroneService {

	ArrayList<Drohne> findAll();

	Drohne save(Drohne drohne);
	
	Drohne findOne(Integer id);

}
