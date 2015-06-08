package com.db.dbs.processor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.db.dbs.enums.StatusCode;
import com.db.dbs.model.Application;
import com.db.dbs.model.Component;
import com.db.dbs.model.Link;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.model.Tenant;
import com.db.dbs.model.User;
import com.db.dbs.utilities.Utilities;

public class ModelProcessor implements Processor{
	
	@Inject
	private ModelContext modelContext;
	
	public void process(Exchange exchange) throws Exception {
		try{
			String routeid = exchange.getFromRouteId();
			String responsejson = "{}";
			
			System.out.println(routeid);

			if(routeid.equals("model-user")){
				User user = modelContext.userRepository.findUserByUsername(Utilities.getInHeader(exchange, "username"));
				if(user!=null){
					responsejson = user.toJson();
				} else {
					throw new Exception("User not found");
				}				
			}
			
			if(routeid.equals("model-application-find")){
				Application application = modelContext.applicationRepository.findApplicationByUrl(Utilities.getInHeader(exchange, "applicationurl"));
				if(application!=null){
					responsejson = application.toJson();
				} else {
					throw new Exception("Application not found based on url");
				}				
			}

			if(routeid.equals("model-tenant")){
				Tenant tenant = modelContext.tenantRepository.findTenantByName(Utilities.getInHeader(exchange, "tenantname"));
				if(tenant!=null){
					responsejson = tenant.toJson();
				} else {
					throw new Exception("Tenant not found.");
				}
			}

			if(routeid.equals("model-tenant-page")){
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), "", Utilities.getInHeader(exchange, "pagename"));
				if(page!=null){
					responsejson = page.toJson();
				} else {
					throw new Exception("Page not found.");
				}
			}

			if(routeid.equals("model-application-list")){
				List<Application> applications = modelContext.applicationRepository.listApplicationsByTenant(Utilities.getInHeader(exchange, "tenantname"));
				responsejson = Utilities.convertObjectToJSON(applications);
			}

			if(routeid.equals("model-application")){
				Application application = modelContext.applicationRepository.findApplicationByName(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"));
				if(application!=null){
					responsejson = application.toJson();
				} else {
					throw new Exception("Application not found.");
				}
			}

			if(routeid.equals("model-page-list")){
				List<Page> pages = modelContext.linkpageRepository.listPagesByApplication(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"));
				responsejson = Utilities.convertObjectToJSON(pages);
			}

			if(routeid.equals("model-page")){
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), Utilities.getInHeader(exchange, "pagename"));
				if(page!=null){
					responsejson = page.toJson();
				} else {
					throw new Exception("Page not found.");
				}
			}

			if(routeid.equals("model-link-find")){
				String linkurl = Utilities.getInHeader(exchange, "linkurl").replace("*", "/");
				Link link = modelContext.linkpageRepository.findLinkByUrl(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), linkurl);
				if(link!=null){
					responsejson = link.toJson();
				} else {
					throw new Exception("Link not found.");
				}
			}

			if(routeid.equals("model-link-page-content")){
				String linkurl = Utilities.getInHeader(exchange, "linkurl").replace("*", "/");
				Link link = modelContext.linkpageRepository.findLinkByUrl(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), linkurl);
				if(link!=null){
					Page page = modelContext.linkpageRepository.findPageByName(link.getTenantname(), link.getAppname(), link.getPagename());
					responsejson = modelContext.contentRepository.findPageAsJson(page.getTenantname(), page.getAppname(), page.getName());
				} else {
					throw new Exception("Page content found found.");
				}
			}
			
			if(routeid.equals("model-component-list")){
				List<Component> components = modelContext.componentRepository.listComponentsByPage(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), Utilities.getInHeader(exchange, "pagename"));
				responsejson = Utilities.convertObjectToJSON(components);
			}

			if(routeid.equals("model-component")){
				Component component = modelContext.componentRepository.findComponentByReference(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), Utilities.getInHeader(exchange, "pagename"), Utilities.getInHeader(exchange, "componentname"));
				if(component!=null){
					responsejson = component.toJson();
				} else {
					throw new Exception("Component not found.");
				}
			}
			
			if(routeid.equals("model-page-byappurl")){
				Application application = modelContext.applicationRepository.findApplicationByUrl(Utilities.getInHeader(exchange, "applicationurl"));
				if(application!=null){
					Page page = modelContext.linkpageRepository.findPageByName(application.getTenantName(), application.getName(), Utilities.getInHeader(exchange, "pagename"));
					if(page!=null){
						responsejson = page.toJson();
					} else {
						throw new Exception("Page not found.");
					}
				} else {
					throw new Exception("Application not found based on url");
				}		
			}
			
			if(routeid.equals("model-component-byappurl")){
				Application application = modelContext.applicationRepository.findApplicationByUrl(Utilities.getInHeader(exchange, "applicationurl"));
				if(application!=null){
					Component component = modelContext.componentRepository.findComponentByReference(application.getTenantName(), application.getName(), Utilities.getInHeader(exchange, "pagename"), Utilities.getInHeader(exchange, "componentname"));
					if(component!=null){
						responsejson = component.toJson();
					} else {
						throw new Exception("Component not found.");
					}
				} else {
					throw new Exception("Application not found based on url");
				}			
			}

			Utilities.setJSONOutput(exchange, StatusCode.Success, responsejson);
		
		} catch (Exception ex) {
			Utilities.setJSONOutput(exchange, StatusCode.Error, Utilities.convertObjectToJSON(ex));
		}
		
	}

}
