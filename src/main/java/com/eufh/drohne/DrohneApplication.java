package com.eufh.drohne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.eufh.drohne.configuration.DemoBusinessConfig;
import com.eufh.drohne.configuration.SecurityConfig;
import com.eufh.drohne.configuration.MainConfiguration;
import com.eufh.drohne.configuration.WebApplicationConfig;

@EnableAutoConfiguration
@Configuration
@Import({ MainConfiguration.class, DemoBusinessConfig.class, SecurityConfig.class, WebApplicationConfig.class })
@EnableJpaRepositories(basePackages = {"com.eufh.drohne.repository"})
//@SpringBootApplication
public class DrohneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrohneApplication.class, args);
	}
}
