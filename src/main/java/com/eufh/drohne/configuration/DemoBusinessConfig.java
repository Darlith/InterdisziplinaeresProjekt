package com.eufh.drohne.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.DroneServiceImpl;
import com.eufh.drohne.business.service.impl.ProcessedOrderServiceImpl;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.repository.DroneRepository;
import com.eufh.drohne.repository.ProcessedOrderRepository;
import com.eufh.drohne.repository.TestRepository;

@Configuration
public class DemoBusinessConfig {

	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private DroneRepository droneRepository;
	
	@Autowired
	private ProcessedOrderRepository processedOrderRepository;
	
	@Bean
	public TestService testService() {
		return new TestServiceImpl(testRepository, null, null);
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