package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db.dbs.exceptions.UsernameAlreadyInUseException;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.Tenant;
import com.db.dbs.model.User;
import com.db.dbs.repository.TenantRepository;

@Repository
public class JdbcTenantRepository implements TenantRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcTenantRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public void updateTenant(Tenant tenant) {
		try {
			jdbcTemplate.update(
					"insert into Tenants (name, description) values (?, ?);",
					tenant.getName(), tenant.getDescription());
		} catch (DuplicateKeyException e) {
			jdbcTemplate.update(
					"update Tenants set description = ? where name = ?;",
					tenant.getDescription(), tenant.getName());		
		}
	}

	public Tenant findTenantByUrl(String url) {
		try{
			return jdbcTemplate.queryForObject("select Tenants.* from Tenants, Apps where Apps.tenantname = Tenants.name and Apps.url = ?",
				rowMapper(), url);
		} catch(Exception ex){
			return null;
		}
	}

	public Tenant findTenantByName(String name) {
		try{
			return jdbcTemplate.queryForObject("select Tenants.* from Tenants where Tenants.name = ?",
					rowMapper(), name);
		} catch(Exception ex){
			return null;
		}
	}
	
	private RowMapper<Tenant> rowMapper(){
		return new RowMapper<Tenant>() {
			public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Tenant(modelContext, rs.getString("name"), rs.getString("description"));
			}
		};
	}
}
