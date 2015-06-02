package com.db.dbs.repository;

import com.db.dbs.exceptions.UsernameAlreadyInUseException;
import com.db.dbs.model.User;

public interface UserRepository {
	
	void createUser(User user) throws UsernameAlreadyInUseException;

	User findUserByUsername(String username);
	
}
