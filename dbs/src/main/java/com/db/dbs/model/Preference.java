package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.db.dbs.enums.ItemType;

public class Preference {

	private String tenantname;
	private String applicationname;
	private ItemType itemtype;
	private String itemname;
	private String name;
	private String value;
	
	public Preference(String tenantName, String applicationName, ItemType itemType, String itemName, String name, String value){
		this.tenantname = tenantName;
		this.applicationname = applicationName;
		this.itemtype = itemType;
		this.itemname = itemName;
		this.name = name;
		this.value = value;
	}

	@JsonIgnore
	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	@JsonIgnore
	public String getApplicationname() {
		return applicationname;
	}

	public void setApplicationname(String applicationname) {
		this.applicationname = applicationname;
	}

	@JsonIgnore
	public ItemType getItemtype() {
		return itemtype;
	}

	public void setItemtype(ItemType itemtype) {
		this.itemtype = itemtype;
	}

	@JsonIgnore
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
