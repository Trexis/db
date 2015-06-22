package com.db.dbs.processor;

import java.io.InputStream;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.db.dbs.enums.StatusCode;
import com.db.dbs.model.Application;
import com.db.dbs.model.Component;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.utilities.Utilities;

public class ThemeAssetProcessor implements Processor{
	
	@Inject
	private ModelContext modelContext;
	
	public void process(Exchange exchange) throws Exception {
		try{
			String routeid = exchange.getFromRouteId();
			InputStream responsecontent = null;
			
			if(routeid.equals("theme-asset-by-applicationurl")){
				Application application = modelContext.applicationRepository.findApplicationByUrl(Utilities.getInHeader(exchange, "applicationurl"));
				if(application!=null){
					String linkurl = Utilities.getInHeader(exchange, "encodedurl");
					linkurl = Utilities.getDecodedURL(linkurl); //the link is encoded in order to be passed as variable in camel route
					int lastidx = linkurl.lastIndexOf("/");
					String relativepathtocontent = linkurl.substring(0, lastidx);
					String filename=linkurl.substring(lastidx+1);
					responsecontent = modelContext.contentRepository.findContentByPath(application.getTenantName(), application.getName(), relativepathtocontent, filename);
				} else {
					throw new Exception("Application not found based on url");
				}				
			}

			exchange.getOut().setBody(responsecontent);
		
		} catch (Exception ex) {
			Utilities.setJSONOutput(exchange, StatusCode.Error, Utilities.convertObjectToJSON(ex));
		}
		
	}

}
