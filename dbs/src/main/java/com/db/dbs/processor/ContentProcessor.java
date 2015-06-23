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
				String pagename = Utilities.getInHeader(exchange, "pagename");
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), "", pagename);
				if(page!=null){
					responsecontent = modelContext.contentRepository.findPageHTML(page.getTenantname(), null, page.getName(), page.isIsapplicationpage());
				} else {
					throw new Exception("Page [" + pagename + "] not found.");
				}
			}

			if(routeid.equals("content-page-html")){
				String pagename = Utilities.getInHeader(exchange, "pagename");
				Page page = modelContext.linkpageRepository.findPageByName(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), pagename);
				if(page!=null){
					responsecontent = modelContext.contentRepository.findPageHTML(page.getTenantname(), page.getAppname(), page.getName(), page.isIsapplicationpage());
				} else {
					throw new Exception("Page [" + pagename + "] not found.");
				}
			}

			if(routeid.equals("content-component-html")){
				String pagename = Utilities.getInHeader(exchange, "pagename");
				String compname = Utilities.getInHeader(exchange, "componentname");
				Component component = modelContext.componentRepository.findComponentByName(Utilities.getInHeader(exchange, "tenantname"), Utilities.getInHeader(exchange, "applicationname"), pagename, compname);
				if(component!=null){
					responsecontent = modelContext.contentRepository.findComponentHTML(component.getTenantname(), component.getAppname(), component.getPagename(), component.getName(), component.getPage().isIsapplicationpage());
				} else {
					throw new Exception("Component [" + compname + "] not found for page [" + pagename + "].");
				}
			}
			
			exchange.getOut().setBody(responsecontent);
		
		} catch (Exception ex) {
			Utilities.setJSONOutput(exchange, StatusCode.Error, Utilities.convertObjectToJSON(ex));
		}
		
	}

}
