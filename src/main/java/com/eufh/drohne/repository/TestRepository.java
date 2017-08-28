package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.Order;

@Component
public interface TestRepository extends PagingAndSortingRepository<Order, String> {

	Order findOne(String id);

	ArrayList<Order> findAll();

	Page<Order> findAll(Pageable pageable);
	
	@SuppressWarnings("unchecked")
	Order save(Order order);
	
	//void saveCoordinates(Coordinates coordinates);

	//@Query("Select x from Drohne as x where x.id = pers") //eigentlich ? anstatt pers
	//Order findByPers(String pers);
}
