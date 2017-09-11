package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;

@Component
public interface TestRepository extends PagingAndSortingRepository<Coordinates, String> {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();

	Page<Coordinates> findAll(Pageable pageable);
	
	
	@SuppressWarnings("unchecked")
	Coordinates save(Coordinates coordinates);
	
	//@Query("Select x from Drohne as x where x.id = pers") //eigentlich ? anstatt pers
	//Order findByPers(String pers);
}
