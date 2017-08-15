package com.eufh.drohne.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.domain.Drohne;

@Controller
public class TestController {

	// @Autowired
	// private AuthenticationManager authenticationManager;

	private TestService testService;

	public TestController(TestService testService) {
		this.testService = testService;
	}

	// @ModelAttribute
	// public void addFormPartSubmitActions(Model model) {
	// // model.addAttribute("versuch", "einWert");
	//
	// }
	// Command to get the Username
	// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// String name = auth.getName(); // get logged in username
	// model.addAttribute("username", name);

	// Login form
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	String login(@ModelAttribute(value = "arbeitsanteil") String user, //
			@ModelAttribute(value = "passwort") String pass) {

		return "login";
	}

	// Login Process
//	@RequestMapping(value = "/index-processLogin", method = RequestMethod.POST)
//	String processLogin(Model model, @ModelAttribute(value = "arbeitsanteil") String user, //
//			@ModelAttribute(value = "passwort") String pass) {
//		if (user.equals("test@web.de") && pass.equals("123")) {
//			model.addAttribute("loginSuccess", true);
//			return "index";
//		}
//		model.addAttribute("loginError", true);
//		return "login";
//	}

//	@RequestMapping(value = "/kontakt", method = RequestMethod.GET)
//	String kontakt(Model model, @RequestParam(value = "x", required = false) String wertFuerX) {
//		model.addAttribute("Eingabe", wertFuerX);
//
//		ArrayList<Person> persons = new ArrayList<Person>();
//		persons = testService.findAll();
//		model.addAttribute("list", persons);
//		return "kontakt";
//	}

//	@RequestMapping(value = "/table", method = RequestMethod.GET)
//	String table() {
//		return "table";
//	}

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
//		Drohne drohne = new Drohne(1, "Kingston", "3");
//		Drohne drohne2 = new Drohne(2, "Budapest", "1");
//		testService.save(drohne);
//		testService.save(drohne2);
		TestServiceImpl test = new TestServiceImpl(null);
		test.startDroneSimulation();

		ArrayList<Drohne> drohnen = new ArrayList<Drohne>();
		drohnen = testService.findAll();
		model.addAttribute("list", drohnen);
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
		model.addAttribute("pageName", "Diagramme");
		return "dashboard";
	}

	@RequestMapping("/notifications")
	String notification(Model model) {
		// Setzt den Namen der Seite auf der validationHeader.html
		model.addAttribute("pageName", "Benachrichtigungen");
		return "notifications";
	}

	@RequestMapping("/")
	String start() {
		return "redirect:dashboard";
	}
}
