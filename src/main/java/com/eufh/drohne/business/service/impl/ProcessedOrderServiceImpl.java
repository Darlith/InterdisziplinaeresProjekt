package com.eufh.drohne.business.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.Coordinates;
import com.eufh.drohne.domain.Order;
import com.eufh.drohne.domain.ProcessedOrder;
import com.eufh.drohne.repository.DroneRepository;
import com.eufh.drohne.repository.ProcessedOrderRepository;
import com.eufh.drohne.repository.TestRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class ProcessedOrderServiceImpl implements ProcessedOrderService {
	
	private ProcessedOrderRepository processedOrderRepository;

	public ProcessedOrderServiceImpl(ProcessedOrderRepository repo) {
		this.processedOrderRepository = repo;
	}

	@Override
	public ArrayList<ProcessedOrder> findAll() {
		return processedOrderRepository.findAll();
	}
	
	@Override
	public ProcessedOrder findOne(Integer id) {
		return processedOrderRepository.findOne(id);
	}

	@Override
	@Transactional
	public ProcessedOrder save(ProcessedOrder order) {
		return processedOrderRepository.save(order);
	}

	public void deleteAll() {
		this.processedOrderRepository.deleteAll();
	}
}

