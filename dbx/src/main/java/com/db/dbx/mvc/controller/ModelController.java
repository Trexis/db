package com.db.dbx.mvc.controller;

import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.config.Constants;
import com.db.dbx.model.Application;
import com.db.dbx.model.Link;
import com.db.dbx.model.Page;
import com.db.dbx.model.User;
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
}
