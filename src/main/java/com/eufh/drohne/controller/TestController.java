package com.eufh.drohne.controller;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.domain.Drohne;
import com.eufh.drohne.domain.Order;

@Controller
public class TestController {

	private TestService testService;
	private DroneService droneService;

	public TestController(TestService testService, DroneService droneService) {
		this.testService = testService;
		this.droneService = droneService;
	}

	// Login form
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	String login(@ModelAttribute(value = "arbeitsanteil") String user, //
			@ModelAttribute(value = "passwort") String pass) {

		return "login";
	}

	@RequestMapping("/validation")
	String validation(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("pageName", "Validation");
		return "validation";
	}

	@RequestMapping("/bepacken")
	String datatablesNet(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Bepacken");
		
		TestServiceImpl demo = new TestServiceImpl(null, testService, droneService);
		demo.startDroneSimulation();
		
//		ArrayList<Order> orders = testService.findAll();
//		model.addAttribute("list", orders);
		return "bepacken";
	}

	@RequestMapping("/charts")
	String charts(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Diagramme");
		return "charts";
	}

	@RequestMapping("/dashboard")
	String dashboard(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Dashboard");
		
		ArrayList<Drohne> drohnen = droneService.findAll();
		model.addAttribute("list", drohnen);
		return "dashboard";
	}

	@RequestMapping("/notifications")
	String notification(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Benachrichtigungen");
		return "notifications";
	}
	
	@RequestMapping("/403")
	String accessDenied(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Access Denied");
		return "error/403";
	}
	
	@RequestMapping("/404")
	String pageNotFound(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Page not found");
		return "error/404";
	}
	
	@RequestMapping("/")
	String start() {
		return "redirect:dashboard";
	}
}
