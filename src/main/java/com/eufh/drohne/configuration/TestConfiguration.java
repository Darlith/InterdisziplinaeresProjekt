package com.eufh.drohne.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.controller.TestController;

@Configuration
@EnableJpaRepositories("com.devk.tron.repository")
public class TestConfiguration {

	@Autowired
	private TestService testService;

	// @Autowired
	// private UserService userService;

	@Bean
	public TestController testController() {
		return new TestController(testService);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
			}
		};
	}
}