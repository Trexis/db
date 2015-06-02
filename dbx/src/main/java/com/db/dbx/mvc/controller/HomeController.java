package com.db.dbx.mvc.controller;

import java.security.Principal;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.db.dbx.utilities.Utilities;

@Controller
public class HomeController {
	
	private final Provider<ConnectionRepository> connectionRepositoryProvider;
	
	@Inject
	public HomeController(Provider<ConnectionRepository> connectionRepositoryProvider) {
		this.connectionRepositoryProvider = connectionRepositoryProvider;
	}

	@RequestMapping("**")
	public String home(Principal currentUser, Model model, HttpServletRequest request) {

		String mapping = null;
		
		if(currentUser!=null){
			/*User user = userRepository.findUserByUsername(currentUser.getName());
			model.addAttribute("connectionsToProviders", getConnectionRepository().findAllConnections());
			model.addAttribute(user);*/
		}

		return "dbxpage";
		
		//String appurl = Utilities.getAppURLFromRequest(request);	
		/*Application app = applicationRepository.findApplicationByUrl(appurl);

		if(app!=null){
			model.addAttribute(app);
			if(!app.isAllowannoymous() && currentUser==null){
				Page page = linkpageRepository.findPageByName(app.getTenantName(), app.getName(), Constants.application401);
				model.addAttribute(page);
				mapping = "dbxpage";
			} else {
				String linkurl = Utils.getLinkURLFromRequest(request);
				Link link = linkpageRepository.findLinkByUrl(app.getTenantName(), app.getName(), linkurl);
				if(link!=null){
					Page page = linkpageRepository.findPageByName(link.getTenantname(), link.getAppname(), link.getPagename());
					if(page!=null){
						model.addAttribute(page);
						mapping = "dbxpage";
					} else {
						Page pagenotfound = linkpageRepository.findPageByName(app.getTenantName(), app.getName(),);
						model.addAttribute(pagenotfound);
						mapping = "dbxpage";						
					}
				} else {
					Page page = linkpageRepository.findPageByName(app.getTenantName(), app.getName(), Constants.application404);
					model.addAttribute(page);
					mapping = "dbxpage";
				}
			}
			
		} else {
			mapping = null;
		}*/
		
		//return mapping;

	}
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
}
