package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Person;
import com.eufh.drohne.repository.TestRepository;

public class TestServiceImpl implements TestService {

	private TestRepository testRepository;

	public TestServiceImpl(TestRepository repo) {
		this.testRepository = repo;
	}

	@Override
	public ArrayList<Person> findAll() {
		return testRepository.findAll();
	}

	@Override
	public Person findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Person> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person findByPers(String pers) {
		return testRepository.findByPers(pers);
	}
}
