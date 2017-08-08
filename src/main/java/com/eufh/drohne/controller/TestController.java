package com.eufh.drohne.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {


	@RequestMapping("/index")
	String index() {
		return "index";
	}

	@RequestMapping("/")
	String start() {
		return "redirect:index";
	}
}
