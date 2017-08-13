package com.eufh.drohne.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.repository.TestRepository;

@Configuration
public class DemoBusinessConfig {

	@Autowired
	private TestRepository testRepository;
	
	@Bean
	public TestService testService() {
		return new TestServiceImpl(testRepository);
	}
}