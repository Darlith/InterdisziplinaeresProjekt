package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Drohne;

@Component
public interface TestRepository extends PagingAndSortingRepository<Drohne, String> {

	Drohne findOne(String id);

	ArrayList<Drohne> findAll();

	Page<Drohne> findAll(Pageable pageable);
	
	Drohne save(Drohne drohne);

	@Query("Select x from Drohne as x where x.id = pers") //eigentlich ? anstatt pers
	Drohne findByPers(String pers);
}
