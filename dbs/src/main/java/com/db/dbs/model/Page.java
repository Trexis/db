package com.db.dbs.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class Page {
	
	private ModelContext modelContext;

	private String tenantname;
	private String appname;
	private String name;
	private String title;
	private boolean isapplicationpage;
	
	private Tenant tenant = null;
	private Application application = null;
	private List<Component> components = new ArrayList<Component>();
	
	public Page(ModelContext modelContext, String tenantName, String appName, String name, String title, boolean isApplicationPage){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.name = name;
		this.title = title;
		this.isapplicationpage = isApplicationPage;
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

	@JsonIgnore
	public boolean isIsapplicationpage() {
		return isapplicationpage;
	}

	public void setIsapplicationpage(boolean isapplicationpage) {
		this.isapplicationpage = isapplicationpage;
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
	
	public List<Component> getComponents(){
		this.components = modelContext.componentRepository.listComponentsByPage(this.tenantname, this.appname, this.name);
		return this.components;
	}
	
	@JsonIgnore	
	public Component getComponent(String componentReference){
		return modelContext.componentRepository.findComponentByReference(this.tenantname, this.appname, this.name, componentReference);
	}
	

	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"page\": " + jsonstring + "}";
	}
}
