package com.eufh.drohne.business.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;

public interface TestService {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();

	Page<Coordinates> findAll(Pageable pageable);
	
	Coordinates save(Coordinates coordinates);

	//void saveCoordinates(Coordinates coordinates);

}
