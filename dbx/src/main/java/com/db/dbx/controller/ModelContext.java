package com.db.dbx.controller;

import javax.inject.Inject;

import com.db.dbx.repository.ApplicationRepository;
import com.db.dbx.repository.ComponentRepository;
import com.db.dbx.repository.LinkPageRepository;
import com.db.dbx.repository.TenantRepository;
import com.db.dbx.repository.UserRepository;

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
}
