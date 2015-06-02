package com.db.dbs.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class Application {

	private ModelContext modelContext;
	
	private String tenantname;
	private String name;
	private String description;
	private String url;
	private boolean allowannoymous;
	
	private Tenant tenant = null;
	private List<Link> links = new ArrayList<Link>();
	private List<Page> apppages = new ArrayList<Page>();

	@Inject
	public Application(ModelContext modelContext, String tenantName, String name, String description, String url, boolean allowAnnoymous) {
		this.modelContext = modelContext;
		this.tenantname = tenantName;
		this.name = name;
		this.description = description;
		this.url = url;
		this.allowannoymous = allowAnnoymous;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAllowannoymous() {
		return allowannoymous;
	}

	public void setAllowannoymous(boolean allowannoymous) {
		this.allowannoymous = allowannoymous;
	}

	
	public String getTenantName() {
		return tenantname;
	}

	public void setTenantName(String tenantname) {
		this.tenantname = tenantname;
	}
	
	@JsonIgnore
	public Tenant getTenant(){
		if(this.tenant==null){
			this.tenant = modelContext.tenantRepository.findTenantByName(this.tenantname);
		}
		return tenant;
	}
	
	public List<Page> getApplicationPages(){
		this.apppages = modelContext.linkpageRepository.listPagesByApplication(this.tenantname, this.name);
		return this.apppages;
	}
	
	public List<Link> getLinks(){
		this.links = modelContext.linkpageRepository.listLinksByApplication(this.tenantname, this.name);
		return this.links;
	}

	@JsonIgnore
	public Page getPage(String pageName){
		return modelContext.linkpageRepository.findPageByName(this.tenantname, this.name, pageName);
	}

	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"application\": " + jsonstring + "}";
	}
}
