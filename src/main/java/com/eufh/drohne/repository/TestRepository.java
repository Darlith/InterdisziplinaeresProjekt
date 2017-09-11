package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;

@Component
public interface TestRepository extends Repository<Coordinates, String> {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();
	
	@SuppressWarnings("unchecked")
	Coordinates save(Coordinates coordinates);
	
	//@Query("Select x from Drohne as x where x.id = pers") //eigentlich ? anstatt pers
	//Order findByPers(String pers);
}
