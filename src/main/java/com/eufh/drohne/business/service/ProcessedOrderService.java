package com.eufh.drohne.business.service;

import java.util.ArrayList;

import com.eufh.drohne.domain.ProcessedOrder;

public interface ProcessedOrderService {

	ArrayList<ProcessedOrder> findAll();

	ProcessedOrder save(ProcessedOrder order);
	
	ProcessedOrder findOne(Integer id);

}
