package com.db.dbx.mvc.controller;

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.db.dbx.config.Constants;
import com.db.dbx.gateway.GatewayDispatcher;
import com.db.dbx.model.DBSApplication;
import com.db.dbx.model.DBSUser;
import com.db.dbx.utilities.Utilities;

@Controller
public class HomeController {
	
	private final Provider<ConnectionRepository> connectionRepositoryProvider;
	
	@Inject
	public HomeController(Provider<ConnectionRepository> connectionRepositoryProvider) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
	}

	@RequestMapping("**")
	public String home(Principal currentUser, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception, AccessDeniedException {

		String mapping = null;
		
		if(currentUser!=null){
			//model.addAttribute("connectionsToProviders", getConnectionRepository().findAllConnections());
			model.addAttribute(currentUser);
		}

		String appurl = Utilities.getAppURLFromRequest(request);

		GatewayDispatcher gateway = new GatewayDispatcher(request, response);
		String jsonappmodel = gateway.DispatchDBS("/model/" + appurl);
		
		if(Utilities.validateDBSResponseJSON(jsonappmodel)){
			
			DBSApplication application = new DBSApplication(jsonappmodel);
			
			if(!application.isAllowAnnoymous()&&currentUser==null){
				throw new AccessDeniedException("Annoymous Access not alloyed. Access denied.");
			} else {
				model.addAttribute(application);
				mapping = "dbxpage";
			}
			
		} else {
			mapping = "404";
		}
			
		return mapping;
	}
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
}
