package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Coordinates;

@Component
public interface CoordinateRepository extends Repository<Coordinates, String> {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();
	
	Coordinates save(Coordinates coordinates);
}
