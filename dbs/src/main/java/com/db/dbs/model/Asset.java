package com.db.dbs.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.db.dbs.enums.AssetType;

public class Asset {

	private AssetType assettype;
	private String location;
	private String checksum;
	
	public Asset(AssetType assetType, String location, String checksum){
		this.assettype = assetType;
		this.location = location;
		this.checksum = checksum;
	}

	@JsonProperty("type")
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
