package com.db.dbs.deployer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.WeakReferenceMonitor.ReleaseListener;

import com.db.dbs.common.DBProperties;
import com.db.dbs.enums.AssetCategory;
import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;
import com.db.dbs.model.Application;
import com.db.dbs.model.Asset;
import com.db.dbs.model.Component;
import com.db.dbs.model.Link;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Page;
import com.db.dbs.model.Preference;
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
		
		//ToDo do unzip logic to workfolder here
		
		return deployFromWorkfolder(cleanUp);
	}
	
	public String deployFromWorkfolder(boolean cleanUp) throws IOException{
		this.workfolder = this.getWorkfolder(dbproperties.getProperty("dbs.deployer.workfolder"));
		String jsonresults = "{}";
		
		workingresponse = "";
		File[] tenantdirs = this.workfolder.listFiles(this.directoryFilter());
		for(File dir: tenantdirs){
			deployTenant(dir, cleanUp);
		}
		jsonresults = "{\"results\":[" + workingresponse + "]}";
		
		return jsonresults;
	}

	/*
	 * PRIVATES
	 */

	private void deployTenant(File tenantDirectory, boolean cleanUp) throws IOException{
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
				deployApplication(tenantname, dir, applicationjson, cleanUp);
			} else {
				deployApplication(tenantname, dir, null, cleanUp);
			}
		}		
	}

	private void deployApplication(String tenantName, File applicationDirectory, String applicationJson, boolean cleanUp) throws IOException{
		String applicationname = null;

		try{
		
			if(applicationJson!=null){
				ObjectMapper mapper = new ObjectMapper();
				JsonNode applicationnode = mapper.readTree(applicationJson);
				
				applicationname = getJsonStringValue(applicationnode, "name");
				if(applicationname!=null){
					Application application = new Application(this.modelContext, tenantName, applicationname, getJsonStringValue(applicationnode, "description"), getJsonStringValue(applicationnode, "url"), getJsonBoolValue(applicationnode, "allowAnonymous"));
					modelContext.applicationRepository.updateApplication(application);
				}
				
				registerApplication(tenantName, applicationname, applicationnode);
				
				appendJsonMessageResponse("Deployed application json: " + applicationname);
			}
			
			if(applicationDirectory!=null){
				if(applicationDirectory.exists()){
					if(applicationname==null) applicationname = convertToValidItemName(applicationDirectory.getName());
					deployApplicationAssets(AssetCategory.CatalogComponent, tenantName, applicationname, applicationDirectory, "/_components", cleanUp);
					deployApplicationAssets(AssetCategory.ApplicationPage, tenantName, applicationname, applicationDirectory, "/_apppages", cleanUp);
					deployApplicationAssets(AssetCategory.Page, tenantName, applicationname, applicationDirectory, "/_pages", cleanUp);
					deployApplicationAssets(AssetCategory.Theme, tenantName, applicationname, applicationDirectory, "/_themes", cleanUp);
					deployApplicationAssets(AssetCategory.Content, tenantName, applicationname, applicationDirectory, "/_content", cleanUp);						
				}
			}
		} catch(JsonParseException jex){
			appendJsonMessageResponse("Failed to deploy application, Bad JSON File" + applicationname);
		} catch(Exception ex){
			appendJsonMessageResponse("Failed to deploy application " + applicationname);
		}
	}
	
	private void registerApplication(String tenantName, String applicationName, JsonNode applicationNode){
		JsonNode applicationpages = applicationNode.get("applicationPages");
		if(applicationpages!=null){
			Iterator<JsonNode> pagenodes = applicationpages.iterator();
			while (pagenodes.hasNext()) {
				JsonNode pagenode = pagenodes.next();
				registerPage(tenantName, applicationName, pagenode, true);
			}
		}

		JsonNode linklinks = applicationNode.get("links");
		if(linklinks!=null){
			Iterator<JsonNode> linknodes = linklinks.iterator();
			while (linknodes.hasNext()) {
				JsonNode linklinknode = linknodes.next();
				registerLink(tenantName, applicationName, "", linklinknode);
			}
		}
	}
	
	private String registerPage(String tenantName, String applicationName, JsonNode pageNode, boolean isApplicationPage){
		String name = getJsonStringValue(pageNode, "name");

		Page page = new Page(modelContext, tenantName, applicationName, name, getJsonStringValue(pageNode, "title"), isApplicationPage);
		modelContext.linkpageRepository.updatePage(page);
		
		JsonNode components = pageNode.get("components");
		if(components!=null){
			Iterator<JsonNode> compnodes = components.iterator();
			while (compnodes.hasNext()) {
				JsonNode compnode = compnodes.next();
				registerComponent(tenantName, applicationName, name, compnode);
			}
		}

		appendJsonMessageResponse("Registered page " + name);
		
		return name;
	}

	private String registerLink(String tenantName, String applicationName, String parentLinkName, JsonNode linkNode){
		String name = getJsonStringValue(linkNode, "name");

		JsonNode pagenode = linkNode.get("page");
		String pagename = registerPage(tenantName, applicationName, pagenode, false);
		
		Link link = new Link(modelContext, tenantName, applicationName, parentLinkName, name, getJsonStringValue(linkNode, "title"), getJsonStringValue(linkNode, "url"), pagename);
		modelContext.linkpageRepository.updateLink(link);
		
		JsonNode linklinks = linkNode.get("links");
		if(linklinks!=null){
			Iterator<JsonNode> linknodes = linklinks.iterator();
			while (linknodes.hasNext()) {
				JsonNode linklinknode = linknodes.next();
				registerLink(tenantName, applicationName, name, linklinknode);
			}
		}

		appendJsonMessageResponse("Registered link " + name);

		return name;
	}

	private String registerComponent(String tenantName, String applicationName, String pageName, JsonNode componentNode){
		String name = getJsonStringValue(componentNode, "name");

		Component comp = new Component(modelContext, tenantName, applicationName, pageName, name, getJsonStringValue(componentNode, "title"));
		modelContext.componentRepository.updateComponent(comp);
		
		JsonNode components = componentNode.get("components");
		if(components!=null){
			Iterator<JsonNode> compnodes = components.iterator();
			while (compnodes.hasNext()) {
				JsonNode compnode = compnodes.next();
				registerComponent(tenantName, applicationName, pageName, compnode);
			}
		}

		appendJsonMessageResponse("Registered component " + name);
		
		return name;
	}

	private void registerPreferences(String tenantName, String applicationName, ItemType itemType, String itemName, JsonNode itemNode){
		
		JsonNode preferences = itemNode.get("preferences");
		if(preferences!=null){
		
			Iterator<JsonNode> preferencenodes = preferences.iterator();
			while (preferencenodes.hasNext()) {
				JsonNode prefnode = preferencenodes.next();
				Preference preference = new Preference(tenantName, applicationName, itemType, itemName, getJsonStringValue(prefnode, "name"), getJsonStringValue(prefnode, "value"));
				modelContext.preferenceRepository.updatePreference(preference);
			}

			appendJsonMessageResponse("Registered properties for item " + itemName);
		}
		
	}

	
	private void deployApplicationAssets(AssetCategory assetCategory, String tenantName, String applicationName, File applicationDirectory, String relativeAssetPath, boolean cleanUp){
		File directory = new File(applicationDirectory.getPath() + relativeAssetPath);
		if(directory.exists()){
			deployAssets(assetCategory, tenantName, applicationName, directory, relativeAssetPath, cleanUp);
			appendJsonMessageResponse("Deployed assets for folder: " + relativeAssetPath);
		}
	}
	
	private void deployAssets(AssetCategory assetCategory, String tenantName, String applicationName, File assetsDirectory, String relativeParentFolderPath, boolean cleanUp){
		File[] assetfiles = assetsDirectory.listFiles();
		for(File file: assetfiles){
			if(file.isDirectory()){
				//components folder under pages
				if(file.getName().equals("_components")){
					deployAssets(AssetCategory.LocalizedComponent, tenantName, applicationName, file, relativeParentFolderPath + "/" + file.getName(), cleanUp);
				} else {
					deployAssets(assetCategory, tenantName, applicationName, file, relativeParentFolderPath + "/" + file.getName(), cleanUp);
				}
			} else {
				try{
					//Todo:  calculate checksum
					String mimetype =  new MimetypesFileTypeMap().getContentType(file);
					Asset asset = new Asset(tenantName, applicationName, assetCategory, relativeParentFolderPath, file.getName(), mimetype, "todochecksum");
					modelContext.assetRepository.updateAsset(asset);
					modelContext.contentRepository.updateItemAsset(tenantName, applicationName, relativeParentFolderPath, file);
					appendJsonMessageResponse("Deployed asset : " + relativeParentFolderPath + "/" + file.getName());
				} catch (Exception ex) {
					appendJsonMessageResponse("FAILED : " + ex.getLocalizedMessage());
				}
			}
		}
	}
	
	/*
	 * UTILITIES
	 */
	
	private String convertToValidItemName(String name){
		return name.replaceAll(" ", "").replace(".html", "");
	}
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
