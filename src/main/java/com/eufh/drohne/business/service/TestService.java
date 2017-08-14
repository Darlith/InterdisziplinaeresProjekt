package com.eufh.drohne.business.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eufh.drohne.domain.Drohne;

public interface TestService {

	Drohne findOne(String id);

	ArrayList<Drohne> findAll();

	Page<Drohne> findAll(Pageable pageable);

	Drohne findByPers(String pers);
	
	Drohne save(Drohne drohne);

}
