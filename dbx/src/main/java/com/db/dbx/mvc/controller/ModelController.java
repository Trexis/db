package com.db.dbx.mvc.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.model.Application;
import com.db.dbx.model.Component;
import com.db.dbx.model.Page;
import com.db.dbx.repository.ApplicationRepository;
import com.db.dbx.utilities.Utils;

@Controller
public class ModelController {

	@Inject
	private ApplicationRepository applicationRepository;

	@RequestMapping(value="/model", method=RequestMethod.GET)
	public String model(Model model, HttpServletRequest request) {
		String mapping = null;

		String appurl = Utils.getAppURLFromRequest(request);	
		Application app = applicationRepository.findApplicationByUrl(appurl);

		if(app!=null){
			model.addAttribute(app);
			mapping = "model";
		} else {
			mapping = null;
		}
		
		return mapping;
	}

	@RequestMapping(value = "/model/page/{pageName}", method = RequestMethod.GET) 
	public String modelPage(Model model, HttpServletRequest request, @PathVariable String pageName) {
		String mapping = null;

		String appurl = Utils.getAppURLFromRequest(request);	
		Application app = applicationRepository.findApplicationByUrl(appurl);

		if(app!=null){
			model.addAttribute(app);
			
			Page page = app.getPage(pageName);
			model.addAttribute("htmlcontent", page.getContent());
			
			mapping = "pagecontent";
		} else {
			mapping = null;
		}
		
		return mapping;
	}

	@RequestMapping(value = "/model/page/{pageName}/component/{componentReference}", method = RequestMethod.GET) 
	public String modelComponent(Model model, HttpServletRequest request, @PathVariable String pageName, @PathVariable String componentReference) {
		String mapping = null;

		String appurl = Utils.getAppURLFromRequest(request);	
		Application app = applicationRepository.findApplicationByUrl(appurl);

		if(app!=null){
			model.addAttribute(app);
			
			Page page = app.getPage(pageName);
			Component component = page.getComponent(componentReference);
			model.addAttribute("htmlcontent", component.getContent());

			mapping = "componentcontent";
		} else {
			mapping = null;
		}
		
		return mapping;
	}}
