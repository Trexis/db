package com.db.dbx.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbx.controller.ModelContext;

public class Tenant {

	private ModelContext modelContext;

	private String name;
	private String description;
	private final String defaultappname;
	
	private List<Application> applications = new ArrayList<Application>();

	public Tenant(ModelContext modelContext, String name, String description, String defaultAppName) {
		this.modelContext = modelContext;
		this.name = name;
		this.description = description;
		this.defaultappname = defaultAppName;
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

	public List<Application> getApplications(){
		this.applications = modelContext.applicationRepository.listApplicationsByTenant(this.name);
		return this.applications;
	}

}
