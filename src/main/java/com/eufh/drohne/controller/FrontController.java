package com.eufh.drohne.controller;

import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.domain.Drohne;

@Controller
public class FrontController {

	private TestService testService;
	private DroneService droneService;
	private ProcessedOrderService processedOrderService;

	public FrontController(TestService testService, DroneService droneService, ProcessedOrderService processedOrderService) {
		this.testService = testService;
		this.droneService = droneService;
		this.processedOrderService = processedOrderService;
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

//		ArrayList<Order> orders = testService.findAll();
//		model.addAttribute("list", orders);
		return "bepacken";
	}

	@RequestMapping("/bestellungen")
	public String orders(Model model) {
		model.addAttribute("pageName", "Bestellungen");
		return "bestellungen";
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
