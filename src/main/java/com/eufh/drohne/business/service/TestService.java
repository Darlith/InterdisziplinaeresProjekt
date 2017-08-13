package com.eufh.drohne.business.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eufh.drohne.domain.Person;

public interface TestService {

	Person findOne(String id);

	ArrayList<Person> findAll();

	Page<Person> findAll(Pageable pageable);

	Person findByPers(String pers);

}
