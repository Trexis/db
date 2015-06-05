package com.db.dbs.processor;

import java.io.InputStream;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.db.dbs.common.DBProperties;
import com.db.dbs.deployer.Deploy;
import com.db.dbs.enums.StatusCode;
import com.db.dbs.model.Component;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.utilities.Utilities;

public class DeployerProcessor implements Processor{
	
	@Inject
	Deploy deploy;

	public void process(Exchange exchange) throws Exception {
		try{
			String routeid = exchange.getFromRouteId();
			String responsejson = "{}";

			if(routeid.equals("deploy-from-workfolder")){
				try{
					responsejson = deploy.deployFromWorkfolder(false);
				} catch(Exception ex) {
					throw new Exception("Failed to deploy.");
				}
			}

			if(routeid.equals("deploy-from-zip")){
				try{
					InputStream zipfile = (InputStream)exchange.getIn().getBody();
					responsejson = deploy.deployFromZip(zipfile, true);					
				} catch(Exception ex) {
					throw new Exception("Failed to deploy from Zip.");
				}
			}
			
			Utilities.setJSONOutput(exchange, StatusCode.Success, responsejson);
			
		} catch (Exception ex) {
			Utilities.setJSONOutput(exchange, StatusCode.Error, Utilities.convertObjectToJSON(ex));
		}
		
	}

}
