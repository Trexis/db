package com.db.dbs.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class Link {

	private ModelContext modelContext;

	private String tenantname;
	private String appname;
	private String parentlinkname;
	private String name;
	private String url;
	private String pagename;
	
	private Tenant tenant = null;
	private Application application = null;
	private Page page = null;
	private List<Link> links;
	
	public Link(ModelContext modelContext, String tenantName, String appName, String parentLinkName, String name, String url, String pageName){
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.appname = appName;
		this.parentlinkname = parentLinkName;
		this.name = name;
		this.url = url;
		this.pagename = pageName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentlinkname;
	}

	public void setParentName(String parentName) {
		this.parentlinkname = parentName;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public List<Link> getLinks(){
		this.links = modelContext.linkpageRepository.listLinksByApplication(this.tenantname, this.appname, this.name);
		return this.links;
	}
	
	public Page getPage(){
		this.page = modelContext.linkpageRepository.findPageByName(this.tenantname, this.appname, this.pagename);
		return this.page;
	}
	

	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"link\": " + jsonstring + "}";
	}
}
