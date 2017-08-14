package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.repository.TestRepository;

public class TestServiceImpl implements TestService {

	private TestRepository testRepository;

	public TestServiceImpl(TestRepository repo) {
		this.testRepository = repo;
	}

	@Override
	public ArrayList<Drohne> findAll() {
		return testRepository.findAll();
	}

	@Override
	public Drohne findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Drohne> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drohne findByPers(String pers) {
		return testRepository.findByPers(pers);
	}

	@Override
	@Transactional
	public Drohne save(Drohne drohne) {
		return testRepository.save(drohne);
	}
}
