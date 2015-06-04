package com.db.dbx.mvc.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.model.DBSApplication;
import com.db.dbx.utilities.Utilities;

@Controller
public class SecuredController {

	@RequestMapping(value="/security/login**", method={RequestMethod.GET, RequestMethod.POST})
	public String securityLogin(Principal currentUser, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String mapping = "login";
		String appurl = Utilities.getAppURLFromRequest(request);

		GatewayDispatcher gateway = new GatewayDispatcher(request, response);
		String jsonappmodel = gateway.DispatchDBS("/model/" + appurl);
		
		if(Utilities.validateDBSResponseJSON(jsonappmodel)){
			
			DBSApplication application = new DBSApplication(jsonappmodel);
			model.addAttribute(application);
			mapping = "dbxpage";
		}
		
		//If we can;t find the application, then we revert to the system login
			
		return mapping;
	}
	
	@RequestMapping(value="/security/enroll**", method=RequestMethod.POST)
	public String securityEnroll() {
		return "enroll";
	}
	@RequestMapping(value="/security/status**", method=RequestMethod.GET)
	public String securityStatus() {
		return "status";
	}
}
