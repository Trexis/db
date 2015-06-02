package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.utilities.Utilities;

public class User {

	private ModelContext modelContext;

	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String tenantname;
	private String defaultappname;

	private Tenant tenant = null;

	public User(ModelContext modelContext, String username, String password, String firstName, String lastName, String tenantName, String defaultAppName) {
		this.modelContext = modelContext;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.tenantname = tenantName;
		this.defaultappname = defaultAppName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	public String getDefaultappname() {
		return defaultappname;
	}

	public void setDefaultappname(String defaultappname) {
		this.defaultappname = defaultappname;
	}

	@JsonIgnore
	public Tenant getTenant(){
		if(this.tenant ==null){
			this.tenant = modelContext.tenantRepository.findTenantByName(this.tenantname);
		}
		return tenant;
	}

	@JsonIgnore
	public String toJson(){
		String jsonstring = Utilities.convertObjectToJSON(this);
		return "{\"user\": " + jsonstring + "}";
	}

}
