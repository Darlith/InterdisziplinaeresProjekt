package com.eufh.drohne.business.service.impl;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.domain.ProcessedOrder;
import com.eufh.drohne.repository.ProcessedOrderRepository;

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

