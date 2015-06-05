package com.db.dbs.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.enums.ItemType;
import com.db.dbs.utilities.Utilities;

public class Component {

	private ModelContext modelContext;

	private String tenantname;
	private String appname;
	private String pagename;
	private String name;
	private String title;
	
	private Tenant tenant = null;
	private Application application = null;
	private Page page = null;
	private List<Preference> preferences;
	private List<Asset> assets;
	
	public Component(ModelContext modelContext, String tenantName, String appName, String pageName, String name, String title){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.pagename = pageName;
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<Asset> getAssets(){
		this.assets = modelContext.assetRepository.listItemAssets(this.tenantname, this.appname, ItemType.Component, this.name);
		return this.assets;
	}

	public List<Preference> getPreferences(){
		this.preferences = modelContext.preferenceRepository.listItemPreferences(this.tenantname, this.appname, ItemType.Component, this.name);
		return this.preferences;
	}
	
	@JsonIgnore
	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	@JsonIgnore
	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	@JsonIgnore
	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	@JsonIgnore
	public Tenant getTenant(){
		if(this.tenant==null){
			this.tenant = modelContext.tenantRepository.findTenantByName(this.tenantname);
		}
		return tenant;
	}
	
	@JsonIgnore
	public Application getApplication(){
		if(this.application==null){
			this.application = modelContext.applicationRepository.findApplicationByName(this.tenantname, this.appname);
		}
		return this.application;
	}
	
	@JsonIgnore
	public Page getPage(){
		this.page = modelContext.linkpageRepository.findPageByName(this.tenantname, this.appname, this.pagename);
		return this.page;
	}
	

	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"component\": " + jsonstring + "}";
	}
}
