package com.eufh.drohne.business.service;

import java.util.ArrayList;

import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Drohne;

public interface TestService {

	Coordinates findOne(String id);

	ArrayList<Coordinates> findAll();

	Coordinates save(Coordinates coordinates);

}
