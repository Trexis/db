package com.db.dbx.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db.dbx.controller.ModelContext;
import com.db.dbx.exceptions.UsernameAlreadyInUseException;
import com.db.dbx.model.Application;
import com.db.dbx.model.Component;
import com.db.dbx.model.Link;
import com.db.dbx.model.Page;
import com.db.dbx.model.Tenant;
import com.db.dbx.model.User;
import com.db.dbx.repository.ApplicationRepository;
import com.db.dbx.repository.ComponentRepository;
import com.db.dbx.repository.LinkPageRepository;
import com.db.dbx.repository.TenantRepository;
import com.db.dbx.repository.UserRepository;

@Repository
public class JdbcComponentRepository implements ComponentRepository {

	private final JdbcTemplate jdbcTemplate;
	
	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcComponentRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Component> listComponentsByPage(String tenantName, String appName, String pageName) {
	    return this.jdbcTemplate.query("select * from Components where tenantname = ? and appname = ? and pagename = ?", new Object[] { tenantName, appName, pageName }, componentRowMapper());
	}

	public Component findComponentByReference(String tenantName, String appName, String pageName, String componentRef) {
		return jdbcTemplate.queryForObject("select * from Components where tenantname = ? and appname = ? and pagename = ? and reference = ?",
				componentRowMapper(), tenantName, appName, pageName, componentRef);
	}

	private RowMapper<Component> componentRowMapper(){
		return new RowMapper<Component>() {
			public Component mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Component(modelContext, rs.getString("tenantname"), rs.getString("appname"), rs.getString("pagename"), rs.getString("reference"), rs.getString("content"));
			}
		};
	}

}
