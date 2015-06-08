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
import org.springframework.util.WeakReferenceMonitor.ReleaseListener;

import com.db.dbs.common.DBProperties;
import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Application;
import com.db.dbs.model.Asset;
import com.db.dbs.model.Component;
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
	private String workingresponse = "";
	
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
		
		workingresponse = "";
		File[] tenantdirs = this.workfolder.listFiles(this.directoryFilter());
		for(File dir: tenantdirs){
			deployTenant(dir);
		}
		jsonresults = "{\"results\":[" + workingresponse + "]}";
		
		return jsonresults;
	}

	/*
	 * PRIVATES
	 */

	private void deployTenant(File tenantDirectory) throws IOException{
		String tenantdescription = tenantDirectory.getName();
		String tenantname = tenantdescription.replace(" ", "").toLowerCase();
		
		Tenant tenant = modelContext.tenantRepository.findTenantByName(tenantname);
		if(tenant==null){
			tenant = new Tenant(modelContext, tenantname, tenantdescription);
			modelContext.tenantRepository.updateTenant(tenant);
			appendJsonMessageResponse("Deployed tenant: " + tenantname);
		}
		
		File[] appdirs = tenantDirectory.listFiles(this.directoryFilter());
		for(File dir: appdirs){
			String appname = dir.getName();
			File appjsonfile = new File(tenantDirectory.getPath() + "/" + appname + ".json");
			if(appjsonfile.exists()){
				String applicationjson = FileUtils.readFileToString(appjsonfile, "UTF-8");
				deployApplication(tenantname, applicationjson);
			} else {
				deployApplication(tenantname, dir);
			}
		}		
	}

	private void deployApplication(String tenantName, File applicationDirectory){
		String applicationname = applicationDirectory.getName();
		Application application = modelContext.applicationRepository.findApplicationByName(tenantName, applicationname);
		if(application==null){
			application = new Application(this.modelContext, tenantName, applicationname, applicationname, applicationname + "." + tenantName + ".com", false);
			modelContext.applicationRepository.updateApplication(application);
			appendJsonMessageResponse("Deployed application: " + applicationname);
		}

		deployPages(tenantName, applicationname, applicationDirectory, true, null);
		deployPages(tenantName, applicationname, applicationDirectory, false, null);
		deployComponents(tenantName, applicationname, null, applicationDirectory, null);

	}
	
	private void deployApplication(String tenantName, String applicationJson) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(applicationJson);
		JsonNode applicationnode = actualObj.findValue("application");
		
		String applicationname = getJsonStringValue(applicationnode, "name");
		Application application = new Application(this.modelContext, tenantName, applicationname, getJsonStringValue(applicationnode, "description"), getJsonStringValue(applicationnode, "url"), getJsonBoolValue(applicationnode, "allowAnnoymous"));
		modelContext.applicationRepository.updateApplication(application);
		appendJsonMessageResponse("Deployed application json: " + applicationname);
		
		File applicationdirectory = new File(this.workfolder.getPath() + "/" + tenantName + "/" + applicationname);
		if(applicationdirectory.exists()){
			deployPages(tenantName, applicationname, applicationdirectory, true, applicationnode.findValues("applicationPages"));
			deployPages(tenantName, applicationname, applicationdirectory, false, applicationnode.findValues("pages"));
			deployComponents(tenantName, applicationname, null, applicationdirectory, null);
		}
		
		deployLinks(tenantName, applicationname, applicationnode.findValues("links"));
	}

	private void deployPages(String tenantName, String applicationName, File applicationDirectory, boolean isApplicationPage, List<JsonNode> jsonData){
		String pagesfolder = "_pages";
		if(isApplicationPage) pagesfolder = "_apppages";
		
		File pagesdir = new File(applicationDirectory.getPath() + "/" + pagesfolder);
		if(pagesdir.exists()){
			File[] filepages = pagesdir.listFiles();
			for(File filepage: filepages){
				JsonNode pageJson = null;  //we will find the json node here in futur
				if(filepage.isDirectory()){
					String pagename = filepage.getName().replace(" ", "");
					File[] htmlfiles = filepage.listFiles(htmlFileFilter());
					if(htmlfiles.length>0){
						File htmlfile = htmlfiles[0];
						pagename = htmlfile.getName().replace(".html", "");
						
						deployPage(tenantName, applicationName, isApplicationPage, htmlfile, pageJson);
						
						//check here if there is a link matching this page already.
						if(!isApplicationPage){
							deployLink(tenantName, applicationName, filepage.getName(), pagename);
						}
					
						//deploy page compponent
						List<JsonNode> componentsJson = null;
						if(pageJson!=null) pageJson.findValues("components");
						deployComponents(tenantName, applicationName, pagename, filepage, componentsJson);
						
					}
					//deploy page assets
					deployAssets(tenantName, applicationName, pagename, filepage, "");
				} else {
					deployPage(tenantName, applicationName, isApplicationPage, filepage, pageJson);
					//check here if there is a link matching this page already.
					String htmlfilename = filepage.getName().replace(".html", "");
					if(!isApplicationPage) {
						deployLink(tenantName, applicationName, htmlfilename, htmlfilename);
					}
				}
			}
		}
	}

	private void deployPage(String tenantName, String applicationName, boolean isApplicationPage, File pageFile, JsonNode pageJson){
		String pagename = pageFile.getName().replace(".html", "");

		try {
			String filecontent = FileUtils.readFileToString(pageFile, "UTF-8");
			modelContext.contentRepository.updatePageHTML(tenantName, applicationName, pagename, filecontent);
			Page page = new Page(modelContext, tenantName, applicationName, pagename, pagename, isApplicationPage);
			modelContext.linkpageRepository.updatePage(page);
			
			//ToDo
			//for now we do nothing with the jsondata, but
			//if we support preferences on pages, then we add it here
			if(pageJson!=null){
			}

			appendJsonMessageResponse("Deployed page : " + pagename);

		} catch (Exception ex) {
			appendJsonMessageResponse("FAILED : " + ex.getLocalizedMessage());
		}
	}
	
	private void deployLinks(String tenantName, String applicationName, List<JsonNode> jsonData){
		if(jsonData!=null){
		}
	}
	
	private void deployLink(String tenantName, String applicationName, String linkTitle, String pageName){
		
		String linkname = linkTitle.replace(" ", "").toLowerCase();
		String linkurl = "/" + linkname;
		if(pageName.toLowerCase().equals("index")) linkurl = "/";
		
		Link link = new Link(this.modelContext, tenantName, applicationName, "", linkname, linkTitle, linkurl, pageName);
		modelContext.linkpageRepository.updateLink(link);
		appendJsonMessageResponse("Deployed Link : " + linkname);
		
	}
	
	private void deployComponents(String tenantName, String applicationName, String parentName, File parentDirectory, List<JsonNode> jsonData){
		File compsdir = new File(parentDirectory.getPath() + "/_components");
		if(compsdir.exists()){
			File[] compfolders = compsdir.listFiles(directoryFilter());
			for(File compfolder: compfolders){
				JsonNode compJson = null;  //we will find the json node here in futur
				File[] htmlfiles = compfolder.listFiles(htmlFileFilter());
				String componentname = compfolder.getName().replace(" ", "");
				if(htmlfiles.length>0){
					File htmlfile = htmlfiles[0];
					deployComponent(tenantName, applicationName, parentName, componentname, htmlfile, compJson);
				}
				deployAssets(tenantName, applicationName, parentName, componentname, compfolder, "");
			
			}
		}
	}
	
	
	private void deployComponent(String tenantName, String applicationName, String parentName, String componentName, File compFile, JsonNode compJson){
		String title = componentName;
		
		try {
			String filecontent = FileUtils.readFileToString(compFile, "UTF-8");
			modelContext.contentRepository.updateComponentHTML(tenantName, applicationName, parentName, componentName, filecontent);
			if(parentName!=null) {
				Component component = new Component(modelContext, tenantName, applicationName,  parentName, componentName, title);
				modelContext.componentRepository.updateComponent(component);
			}
			
			//ToDo
			//for now we do nothing with the jsondata, but
			//if we support preferences on pages, then we add it here
			if(compJson!=null){
			}

			appendJsonMessageResponse("Deployed component : " + componentName);

		} catch (Exception ex) {
			appendJsonMessageResponse("FAILED : " + ex.getLocalizedMessage());
		}
	}
	
	private void deployAssets(String tenantName, String applicationName, String pageName, File assetsDirectory, String relativeParentFolderPath){
		deployAssets(tenantName, applicationName, pageName, null, assetsDirectory, relativeParentFolderPath);
	}
	
	private void deployAssets(String tenantName, String applicationName, String pageName, String componentName, File assetsDirectory, String relativeParentFolderPath){
		File[] assetfiles = assetsDirectory.listFiles();
		for(File file: assetfiles){
			if(file.isDirectory()){
				//components get deployed seperate
				if(!file.getName().equals("_components")){
					deployAssets(tenantName, applicationName, pageName, componentName, file, relativeParentFolderPath + "/" + file.getName());
				}
			} else {
				//Dont deploy the page html file itself, as its already deployed
				if(!file.getName().endsWith(".html")){
					try{
						if(pageName==null){
							modelContext.contentRepository.updateItemAsset(tenantName, applicationName, pageName, componentName, relativeParentFolderPath, file);
							Asset asset = new Asset(tenantName, applicationName, ItemType.Component, componentName, AssetType.CSS, relativeParentFolderPath + "/" + file.getName(), "blabla");
							modelContext.assetRepository.updateAsset(asset);
						} else {
							modelContext.contentRepository.updateItemAsset(tenantName, applicationName, pageName, relativeParentFolderPath, file);
							Asset asset = new Asset(tenantName, applicationName, ItemType.Page, pageName, AssetType.CSS, relativeParentFolderPath + "/" + file.getName(), "blabla");
							modelContext.assetRepository.updateAsset(asset);
						}
						appendJsonMessageResponse("Deployed asset : " + relativeParentFolderPath + "/" + file.getName());

					} catch (Exception ex) {
						appendJsonMessageResponse("FAILED : " + ex.getLocalizedMessage());
					}
				}
			}
		}
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
	private void appendJsonMessageResponse(String message){
		if(!message.equals("")){
			if(!workingresponse.equals("")) workingresponse+= ",";
			workingresponse += "\"" +  message + "\"";
		}
	}
}
