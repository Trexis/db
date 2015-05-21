package com.db.dbx.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbx.controller.ModelContext;

public class Component {

	private ModelContext modelContext;

	private String tenantname;
	private String appname;
	private String pagename;
	private String reference;
	private String content;
	
	private Tenant tenant = null;
	private Application application = null;
	private Page page = null;
	
	public Component(ModelContext modelContext, String tenantName, String appName, String pageName, String reference, String content){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.pagename = pageName;
		this.reference = reference;
		this.content = content;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@JsonIgnore
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
}
