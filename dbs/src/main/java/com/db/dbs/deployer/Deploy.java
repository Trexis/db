package com.db.dbs.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.db.dbs.common.DBProperties;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Application;
import com.db.dbs.model.Link;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.model.Tenant;

public class Deploy {

	@Inject
	ModelContext modelContext;

	@Inject
	DBProperties dbproperties;
	
	private File workfolder = null;
	
	public Deploy(){
	}
	
	public String deployFromZip(InputStream zipFile, boolean cleanUp) throws IOException{
		this.workfolder = this.getWorkfolder(dbproperties.getProperty("dbs.deployer.workfolder"));
		
		// do unzip logic to workfolder here
		return deployFromWorkfolder(cleanUp);
	}
	
	public String deployFromWorkfolder(boolean cleanUp) throws IOException{
		this.workfolder = this.getWorkfolder(dbproperties.getProperty("dbs.deployer.workfolder"));
		
		String jsonresults = "{}";
		
		String tenantjsonresponse = "";
		File[] tenantdirs = this.workfolder.listFiles(this.directoryFilter());
		for(File dir: tenantdirs){
			if(!tenantjsonresponse.equals("")) tenantjsonresponse += ",";
			tenantjsonresponse += deployTenant(dir);
		}
		jsonresults = "{\"results\":[" + tenantjsonresponse + "]}";
		
		return jsonresults;
	}

	/*
	 * PRIVATES
	 */

	private String deployTenant(File tenantDirectory) throws IOException{
		String response = "";
		String tenantdescription = tenantDirectory.getName();
		String tenantname = tenantdescription.replace(" ", "").toLowerCase();
		
		Tenant tenant = modelContext.tenantRepository.findTenantByName(tenantname);
		if(tenant==null){
			tenant = new Tenant(modelContext, tenantname, tenantdescription);
			modelContext.tenantRepository.updateTenant(tenant);
			response = appendJsonMessageResponse(response, "Deployed tenant: " + tenantname);
		}
		
		File[] appdirs = tenantDirectory.listFiles(this.directoryFilter());
		for(File dir: appdirs){
			String appname = dir.getName();
			File appjsonfile = new File(tenantDirectory.getPath() + "/" + appname + ".json");
			if(appjsonfile.exists()){
				String applicationjson = FileUtils.readFileToString(appjsonfile, "UTF-8");
				response = appendJsonMessageResponse(response, deployApplication(tenantname, applicationjson));
			} else {
				response = appendJsonMessageResponse(response, deployApplication(tenantname, dir));
			}
		}		
		
		return response;
	}

	private String deployApplication(String tenantName, File applicationDirectory){
		String response = "";
		
		String applicationname = applicationDirectory.getName();
		Application application = modelContext.applicationRepository.findApplicationByName(tenantName, applicationname);
		if(application==null){
			application = new Application(this.modelContext, tenantName, applicationname, applicationname, applicationname + "." + tenantName + ".com", false);
			modelContext.applicationRepository.updateApplication(application);
			response = appendJsonMessageResponse(response, "Deployed application: " + applicationname);
		}

		response = appendJsonMessageResponse(response, deployPages(tenantName, applicationname, applicationDirectory, true, null));
		response = appendJsonMessageResponse(response, deployPages(tenantName, applicationname, applicationDirectory, false, null));

		return response;
	}
	
	private String deployApplication(String tenantName, String applicationJson) throws IOException{
		String response = "";
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(applicationJson);
		JsonNode applicationnode = actualObj.findValue("application");
		
		String applicationname = getJsonStringValue(applicationnode, "name");
		Application application = new Application(this.modelContext, tenantName, applicationname, getJsonStringValue(applicationnode, "description"), getJsonStringValue(applicationnode, "url"), getJsonBoolValue(applicationnode, "allowAnnoymous"));
		modelContext.applicationRepository.updateApplication(application);
		response = appendJsonMessageResponse(response, "Deployed application json: " + applicationname);
		
		File applicationdirectory = new File(this.workfolder.getPath() + "/" + tenantName + "/" + applicationname);
		if(applicationdirectory.exists()){
			response = appendJsonMessageResponse(response, deployPages(tenantName, applicationname, applicationdirectory, true, applicationnode.findValues("applicationPages")));
			response = appendJsonMessageResponse(response, deployPages(tenantName, applicationname, applicationdirectory, false, applicationnode.findValues("pages")));
		}
		
		response = appendJsonMessageResponse(response, deployLinks(tenantName, applicationname, applicationnode.findValues("links")));

		return response;
	}

	private String deployPages(String tenantName, String applicationName, File applicationDirectory, boolean isApplicationPage, List<JsonNode> jsonData){
		String response = "";
		
		//ToDo
		//for now we do nothing with the jsondata, but
		//if we support preferences on pages, then we add it here
		if(jsonData!=null){
		
		}
		
		String pagesfolder = "_pages";
		if(isApplicationPage) pagesfolder = "_apppages";
		
		File apppagesdir = new File(applicationDirectory.getPath() + "/" + pagesfolder);
		if(apppagesdir.exists()){
			File[] filepages = apppagesdir.listFiles();
			for(File filepage: filepages){
				JsonNode pageJson = null;  //we will find the json node here in futur
				if(filepage.isDirectory()){
					File[] htmlfiles = filepage.listFiles(htmlFileFilter());
					if(htmlfiles.length>0){
						File htmlfile = htmlfiles[0];
						String htmlfilename = htmlfile.getName().replace(".html", "");
						response = appendJsonMessageResponse(response, deployPage(tenantName, applicationName, isApplicationPage, htmlfile, pageJson));					
						//check here if there is a link matching this page already.
						if(!isApplicationPage) response = appendJsonMessageResponse(response, deployLink(tenantName, applicationName, filepage.getName(), htmlfilename));					
					}
					response = appendJsonMessageResponse(response, deployAssets(tenantName, applicationName, ItemType.Page, filepage.getName(), filepage));
				} else {
					response = appendJsonMessageResponse(response, deployPage(tenantName, applicationName, isApplicationPage, filepage, pageJson));
					//check here if there is a link matching this page already.
					String htmlfilename = filepage.getName().replace(".html", "");
					if(!isApplicationPage) response = appendJsonMessageResponse(response, deployLink(tenantName, applicationName, htmlfilename, htmlfilename));					
				}
			}
		}
		
		return response;
	}

	private String deployPage(String tenantName, String applicationName, boolean isApplicationPage, File pageFile, JsonNode pageJson){

		String response = "";
		String pagename = pageFile.getName().replace(".html", "");

		try {
			String filecontent = FileUtils.readFileToString(pageFile, "UTF-8");
			modelContext.contentRepository.createPage(tenantName, applicationName, pagename, filecontent);
			Page page = new Page(modelContext, tenantName, applicationName, pagename, isApplicationPage);
		
			//ToDo
			//for now we do nothing with the jsondata, but
			//if we support preferences on pages, then we add it here
			if(pageJson!=null){
			}

			response = appendJsonMessageResponse(response, "Deployed page : " + pagename);

		} catch (Exception ex) {
			response = appendJsonMessageResponse(response, "FAILED : " + ex.getLocalizedMessage());
		}

		return response;
	}
	
	private String deployLinks(String tenantName, String applicationName, List<JsonNode> jsonData){
		if(jsonData!=null){
		}
		return "";
	}
	
	private String deployLink(String tenantName, String applicationName, String linkTitle, String pageName){
		String response = "";
		
		String linkname = linkTitle.replace(" ", "").toLowerCase();
		String linkurl = "/" + linkname;
		if(pageName.toLowerCase().equals("index")) linkurl = "/";
		
		
		Link link = new Link(this.modelContext, tenantName, applicationName, null, linkname, linkurl, pageName);
		
		return response;
	}
	
	private String deployAssets(String tenantName, String applicationName, ItemType itemType, String itemName, File assetsDirectory){
		return "";
	}
	
	/*
	 * UTILITIES
	 */
	private Boolean getJsonBoolValue(JsonNode parentNode, String property){
		JsonNode node = parentNode.findValue(property);
		if(node!=null){
			return node.getValueAsBoolean();
		} else {
			return null;
		}
	}
	private String getJsonStringValue(JsonNode parentNode, String property){
		JsonNode node = parentNode.findValue(property);
		if(node!=null){
			return node.getValueAsText();
		} else {
			return null;
		}
	}
	
	private File getWorkfolder(String rootFolder){
		File file = new File(rootFolder + "/deploy");
		if(!file.exists()){
			file.mkdir();
		}
		return file;
	}
	
	private FilenameFilter directoryFilter(){
		return new FilenameFilter() {
		  //@Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		};
	}
	private FilenameFilter htmlFileFilter(){
		return new FilenameFilter() {
		  //@Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).getName().endsWith(".html");
		  }
		};
	}	
	private String appendJsonMessageResponse(String response, String message){
		String result = response;
		if(!result.equals("")) result+= ",";
		result += "\"" +  message + "\"";
		return result;
	}
}
