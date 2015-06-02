package com.db.dbs.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db.dbs.exceptions.UsernameAlreadyInUseException;
import com.db.dbs.model.ModelContext;
import com.db.dbs.model.User;
import com.db.dbs.repository.UserRepository;

@Repository
public class JdbcUserRepository implements UserRepository {

	private final JdbcTemplate jdbcTemplate;
	private final PasswordEncoder passwordEncoder;

	@Inject
	private ModelContext modelContext;
	
	@Inject
	public JdbcUserRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void createUser(User user) throws UsernameAlreadyInUseException {
		try {
			jdbcTemplate.update(
					"insert into Users (firstName, lastName, username, password, tenantname, defaultappname) values (?, ?, ?, ?, ?, ?)",
					user.getFirstName(), user.getLastName(), user.getUsername(),
					passwordEncoder.encode(user.getPassword()), user.getTenantname(), user.getDefaultappname());
		} catch (DuplicateKeyException e) {
			throw new UsernameAlreadyInUseException(user.getUsername());
		}
	}

	public User findUserByUsername(String username) {
		return jdbcTemplate.queryForObject("select * from Users where username = ?",
				new RowMapper<User>() {
					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new User(modelContext, rs.getString("username"), null, rs.getString("firstName"), 
								rs.getString("lastName"), rs.getString("tenantname"), rs.getString("defaultappname"));
					}
				}, username);
	}

}
