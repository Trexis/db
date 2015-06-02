package com.db.dbs.repository;

import com.db.dbs.model.Tenant;

public interface TenantRepository {
	
	Tenant findTenantByUrl(String url);
	Tenant findTenantByName(String name);
	
}
