package com.eufh.drohne.business.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;

public interface TestService {

	Order findOne(String id);

	ArrayList<Order> findAll();

	Page<Order> findAll(Pageable pageable);
	
	Order save(Order order);

	Coordinates save(Coordinates coordinates);

	//void saveCoordinates(Coordinates coordinates);

}
