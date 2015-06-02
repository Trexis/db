package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class Component {

	private ModelContext modelContext;

	private String tenantname;
	private String appname;
	private String pagename;
	private String name;
	
	private Tenant tenant = null;
	private Application application = null;
	private Page page = null;
	
	public Component(ModelContext modelContext, String tenantName, String appName, String pageName, String name){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.pagename = pageName;
		this.name = name;
	}

	public String getContentReference() {
		return "/" + this.tenantname + "/" + this.appname + "/" + this.pagename + "/" + this.name + ".html";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
