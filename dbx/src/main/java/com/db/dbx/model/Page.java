package com.db.dbx.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbx.controller.ModelContext;

public class Page {
	
	private ModelContext modelContext;

	private final String tenantname;
	private final String appname;
	private String name;
	private String content;
	private boolean isapplicationpage;
	
	private Tenant tenant = null;
	private Application application = null;
	private List<Component> components = new ArrayList<Component>();
	
	public Page(ModelContext modelContext, String tenantName, String appName, String name, String content, boolean isApplicationPage){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.name = name;
		this.content = content;
		this.isapplicationpage = isApplicationPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isIsapplicationpage() {
		return isapplicationpage;
	}

	public void setIsapplicationpage(boolean isapplicationpage) {
		this.isapplicationpage = isapplicationpage;
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
}
