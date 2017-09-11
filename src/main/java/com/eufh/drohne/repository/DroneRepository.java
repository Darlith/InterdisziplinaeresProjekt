package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;

@Component
public interface DroneRepository extends Repository<Drohne, String> {

	ArrayList<Drohne> findAll();
	
	Drohne save(Drohne drohne);
	
}
