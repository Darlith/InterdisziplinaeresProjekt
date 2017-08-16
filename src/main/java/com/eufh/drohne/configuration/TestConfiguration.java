package com.eufh.drohne.configuration;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.controller.TestController;

@Configuration
@EnableJpaRepositories("com.devk.tron.repository")
public class TestConfiguration {

	@Autowired
	private TestService testService;

	@Bean
	public TestController testController() {
		return new TestController(testService);
	}
	
//	@ExceptionHandler(NoHandlerFoundException.class)
//    public ModelAndView handleError404(HttpServletRequest request, Exception e)   {
//        return new ModelAndView("404");
//    }
}