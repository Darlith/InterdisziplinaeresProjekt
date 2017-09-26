package com.eufh.drohne.business.service;

import java.util.ArrayList;

import com.eufh.drohne.domain.Coordinates;

public interface CoordinateService {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();

	Coordinates save(Coordinates coordinates);

}
