package com.db.dbx.repository;

import com.db.dbx.model.Tenant;

public interface TenantRepository {
	
	Tenant findTenantByUrl(String url);
	Tenant findTenantByName(String name);
	
}
