package com.db.dbs.processor;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.db.dbs.enums.StatusCode;
import com.db.dbs.model.Component;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.utilities.Utilities;

public class ContentProcessor implements Processor{
	
	@Inject
	private ModelContext modelContext;
	
	public void process(Exchange exchange) throws Exception {
		try{
			String routeid = exchange.getFromRouteId();
			String responsecontent = "";

			if(routeid.equals("content-tenant-page-html")){
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), "", Utilities.getInHeader(exchange, "pagename"));
				if(page!=null){
					responsecontent = modelContext.contentRepository.findPageContent(page.getTenantname(), null, page.getName());
				} else {
					throw new Exception("Page not found.");
				}
			}

			if(routeid.equals("content-page-html")){
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), Utilities.getInHeader(exchange, "pagename"));
				if(page!=null){
					responsecontent = modelContext.contentRepository.findPageContent(page.getTenantname(), page.getAppname(), page.getName());
				} else {
					throw new Exception("Page not found.");
				}
			}

			if(routeid.equals("content-component-html")){
				Component component = modelContext.componentRepository.findComponentByReference(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), Utilities.getInHeader(exchange, "pagename"), Utilities.getInHeader(exchange, "componentname"));
				if(component!=null){
					responsecontent = modelContext.contentRepository.findComponentContent(component.getTenantname(), component.getAppname(), component.getPagename(), component.getName());
				} else {
					throw new Exception("Component not found.");
				}
			}
			
			exchange.getOut().setBody(responsecontent);
		
		} catch (Exception ex) {
			Utilities.setJSONOutput(exchange, StatusCode.Error, Utilities.convertObjectToJSON(ex));
		}
		
	}

}
