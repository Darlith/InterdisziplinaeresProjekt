package com.eufh.drohne.configuration;

import com.eufh.drohne.controller.PartialController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.controller.FrontController;

@Configuration
@EnableJpaRepositories("com.eufh.drohne.repository")
public class MainConfiguration {

	@Autowired
	private TestService testService;
	
	@Autowired
	private DroneService droneService;
	
	@Autowired
	private ProcessedOrderService processedOrderService;

	@Bean
	public FrontController droneController() {
		return new FrontController(testService, droneService, processedOrderService);
	}

	@Bean
	public PartialController restController() {
		return new PartialController(processedOrderService, testService, droneService);
	}
}