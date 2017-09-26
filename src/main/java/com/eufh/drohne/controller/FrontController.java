package com.eufh.drohne.controller;

import java.util.ArrayList;

import com.eufh.drohne.analytics.AnalyticsDTO;
import com.eufh.drohne.analytics.AnalyticsService;
import com.eufh.drohne.domain.ProcessedOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.CoordinateService;
import com.eufh.drohne.domain.Drohne;

@SuppressWarnings("rawtypes")
@Controller
public class FrontController {

	private CoordinateService coordinateService;
	private DroneService droneService;
	private ProcessedOrderService processedOrderService;

	public FrontController(CoordinateService testService, DroneService droneService, ProcessedOrderService processedOrderService) {
		this.coordinateService = testService;
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
		SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("pageName", "Validation");
		return "validation";
	}

	@RequestMapping("/bestellungen")
	public String orders(Model model) {
		model.addAttribute("pageName", "Bestellungen");
		return "bestellungen";
	}

	@RequestMapping("/drohnen")
	public String drones(Model model) {
		model.addAttribute("pageName", "Drohnen");

		ArrayList<Drohne> drones = this.droneService.findAll();
		model.addAttribute("drones", drones);

		return "drohnen";
	}

	@RequestMapping(value = "/drones/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteDrone(@PathVariable("id") int droneId) {
		this.droneService.deleteOne(droneId);
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/orders/delete", method = RequestMethod.DELETE)
	public ResponseEntity deleteOrders() {
		this.processedOrderService.deleteAll();
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/drones/add", method = RequestMethod.POST)
	public @ResponseBody Drohne addDrone() {
		Drohne newDrone = new Drohne();
		this.droneService.save(newDrone);
		return newDrone;
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

		ArrayList<ProcessedOrder> orders = this.processedOrderService.findAll();
		ArrayList<Drohne> drones = this.droneService.findAll();

		AnalyticsService analyticsService = new AnalyticsService(this.coordinateService);
		AnalyticsDTO analytics = analyticsService.getAnalyzedData(orders, drones);

		model.addAttribute("analytics", analytics);
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
