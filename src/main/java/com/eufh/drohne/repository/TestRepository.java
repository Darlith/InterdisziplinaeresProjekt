package com.eufh.drohne.repository;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import com.eufh.drohne.domain.Person;

@Component
public interface TestRepository extends PagingAndSortingRepository<Person, String> {

	Person findOne(String id);

	ArrayList<Person> findAll();

	Page<Person> findAll(Pageable pageable);

	@Query("Select x from Person as x where x.personalnummer = ?")
	Person findByPers(String pers);
}
