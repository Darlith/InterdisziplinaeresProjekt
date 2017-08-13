package com.eufh.drohne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.eufh.drohne.configuration.DemoBusinessConfig;
import com.eufh.drohne.configuration.SecurityConfig;
import com.eufh.drohne.configuration.TestConfiguration;
import com.eufh.drohne.repository.TestRepository;

@EnableAutoConfiguration
@Configuration
@Import({ TestConfiguration.class, DemoBusinessConfig.class, SecurityConfig.class })
@EnableJpaRepositories(basePackages = {"com.eufh.drohne.repository"})
//@SpringBootApplication
public class DrohneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrohneApplication.class, args);
	}
}
