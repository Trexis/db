package com.db.dbx.mvc.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.utilities.Utilities;

@Controller
public class ModelController {

	@RequestMapping(value="/_model", method=RequestMethod.GET)
	public String model(Model model, HttpServletRequest request) {
		model.addAttribute("viewname", "model");
		return "model";
	}

	@RequestMapping(value = "/_model/page/{pageName}", method = RequestMethod.GET) 
	public String modelPage(Model model, HttpServletRequest request, @PathVariable String pageName) {
		model.addAttribute("viewname", "model_page");
		model.addAttribute("pageName", pageName);
		return "model_page";
	}

	@RequestMapping(value = "/_model/page/{pageName}/component/{componentName}", method = RequestMethod.GET) 
	public String modelComponent(Model model, HttpServletRequest request, @PathVariable String pageName, @PathVariable String componentName) {
		model.addAttribute("viewname", "model_component");
		model.addAttribute("pageName", pageName);
		model.addAttribute("componentName", componentName);
		return "model_component";
	}}
