package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import com.db.dbs.model.Application;
import com.db.dbs.model.ModelContext;
import com.db.dbs.repository.ApplicationRepository;

@Repository
public class JdbcApplicationRepository implements ApplicationRepository {

	private final JdbcTemplate jdbcTemplate;
	
	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcApplicationRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Application findApplicationByName(String tenantName, String appName) {
		try{
			return jdbcTemplate.queryForObject("select * from Apps where tenantname = ? and name = ?",
					applicationRowMapper(), tenantName, appName);			
		} catch(Exception ex){
			return null;
		}
	}
	
	public Application findApplicationByUrl(String url) {
		try{
			return jdbcTemplate.queryForObject("select * from Apps where url = ?",
					applicationRowMapper(), url);			
		} catch(Exception ex){
			return null;
		}
	}
	
	public List<Application> listApplicationsByTenant(String tenantName) {
		try{
			return this.jdbcTemplate.query("select * from Apps where tenantname = ?", new Object[] { tenantName }, applicationRowMapper());
		} catch(Exception ex){
			return null;
		}
	}
	
	private RowMapper<Application> applicationRowMapper(){
		return new RowMapper<Application>() {
			public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
				boolean allowannoymous = (rs.getInt("allowannoymous")==1);
				return new Application(modelContext, rs.getString("tenantname"), rs.getString("name"), rs.getString("description"), rs.getString("url"), allowannoymous);
			}
		};
	}

}
