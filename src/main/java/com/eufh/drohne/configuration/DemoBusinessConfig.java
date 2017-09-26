package com.eufh.drohne.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.CoordinateService;
import com.eufh.drohne.business.service.impl.DroneServiceImpl;
import com.eufh.drohne.business.service.impl.ProcessedOrderServiceImpl;
import com.eufh.drohne.business.service.impl.CoordinateServiceImpl;
import com.eufh.drohne.repository.DroneRepository;
import com.eufh.drohne.repository.ProcessedOrderRepository;
import com.eufh.drohne.repository.CoordinateRepository;

@Configuration
public class DemoBusinessConfig {

	@Autowired
	private CoordinateRepository coordinateRepository;
	
	@Autowired
	private DroneRepository droneRepository;
	
	@Autowired
	private ProcessedOrderRepository processedOrderRepository;
	
	@Bean
	public CoordinateService coordinateService() {
		return new CoordinateServiceImpl(coordinateRepository);
	}
	
	@Bean
	public DroneService droneService() {
		return new DroneServiceImpl(droneRepository);
	}
	
	@Bean
	public ProcessedOrderService processedOrderService() {
		return new ProcessedOrderServiceImpl(processedOrderRepository);
	}
}