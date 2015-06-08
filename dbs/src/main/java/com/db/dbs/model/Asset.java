package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.db.dbs.enums.AssetType;
import com.db.dbs.enums.ItemType;

public class Asset {

	private String tenantname;
	private String appname;
	private String itemname;
	private ItemType itemtype;
	private AssetType assettype;
	private String location;
	private String checksum;
	
	public Asset(String tenantName, String appName, ItemType itemType, String itemName, AssetType assetType, String location, String checksum){
		this.tenantname = tenantName;
		this.appname = appName;
		this.itemtype = itemType;
		this.itemname = itemName;
		this.assettype = assetType;
		this.location = location;
		this.checksum = checksum;
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
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	@JsonIgnore
	public ItemType getItemtype() {
		return itemtype;
	}

	public void setItemtype(ItemType itemtype) {
		this.itemtype = itemtype;
	}

	public AssetType getAssettype() {
		return assettype;
	}

	public void setAssettype(AssetType assettype) {
		this.assettype = assettype;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
}
