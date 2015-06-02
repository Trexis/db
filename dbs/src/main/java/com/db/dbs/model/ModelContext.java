package com.db.dbs.model;

import javax.inject.Inject;

import com.db.dbs.repository.ApplicationRepository;
import com.db.dbs.repository.ComponentRepository;
import com.db.dbs.repository.ContentRepository;
import com.db.dbs.repository.LinkPageRepository;
import com.db.dbs.repository.TenantRepository;
import com.db.dbs.repository.UserRepository;

public class ModelContext {

	@Inject
	public ApplicationRepository applicationRepository;
	
	@Inject
	public LinkPageRepository linkpageRepository;
	
	@Inject
	public TenantRepository tenantRepository;
	
	@Inject
	public UserRepository userRepository;

	@Inject
	public ComponentRepository componentRepository;

	@Inject
	public ContentRepository contentRepository;
}
