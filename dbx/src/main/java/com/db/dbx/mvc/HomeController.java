package com.db.dbx.mvc;

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.dbx.config.Constants;
import com.db.dbx.model.Application;
import com.db.dbx.model.Link;
import com.db.dbx.model.Page;
import com.db.dbx.model.Tenant;
import com.db.dbx.model.User;
import com.db.dbx.repository.ApplicationRepository;
import com.db.dbx.repository.LinkPageRepository;
import com.db.dbx.repository.TenantRepository;
import com.db.dbx.repository.UserRepository;
import com.db.dbx.utilities.Utils;

@Controller
public class HomeController {
	
	private final Provider<ConnectionRepository> connectionRepositoryProvider;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private ApplicationRepository applicationRepository;

	@Inject
	private LinkPageRepository linkpageRepository;

	@Inject
	public HomeController(Provider<ConnectionRepository> connectionRepositoryProvider) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
	}

	@RequestMapping("**")
	public String home(Principal currentUser, Model model, HttpServletRequest request) {

		String mapping = null;
		
		//http://bank1.db.com:8080/dbx/sitea/
		//getSchema: http
		//getServerName: bank1.db.com
		//getRequestURI: /dbx/sitea/
		//getServerPort:  8080
		//getContextPath: /dbx
		//getServletPath: /sitea/

		if(currentUser!=null){
			User user = userRepository.findUserByUsername(currentUser.getName());
			model.addAttribute("connectionsToProviders", getConnectionRepository().findAllConnections());
			model.addAttribute(user);
		}

		String appurl = Utils.getAppURLFromRequest(request);	
		Application app = applicationRepository.findApplicationByUrl(appurl);

		if(app!=null){
			model.addAttribute(app);
			if(!app.isAllowannoymous() && currentUser!=null){
				Page page = linkpageRepository.findPageByName(app.getTenantName(), app.getName(), Constants.application401);
				model.addAttribute(page);
				mapping = page.getName();
			} else {
				String linkurl = Utils.getLinkURLFromRequest(request);
				Link link = linkpageRepository.findLinkByUrl(app.getTenantName(), app.getName(), linkurl);
				if(link!=null){
					Page page = linkpageRepository.findPageByName(link.getTenantname(), link.getAppname(), link.getPagename());
					model.addAttribute(page);
					mapping = page.getName();
				} else {
					Page page = linkpageRepository.findPageByName(app.getTenantName(), app.getName(), Constants.application404);
					model.addAttribute(page);
					mapping = page.getName();
				}
			}
			
		} else {
			mapping = null;
		}
		
		return mapping;

	}
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
}
