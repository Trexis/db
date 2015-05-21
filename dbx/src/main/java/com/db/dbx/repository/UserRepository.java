package com.db.dbx.repository;

import com.db.dbx.exceptions.UsernameAlreadyInUseException;
import com.db.dbx.model.User;

public interface UserRepository {
	
	void createUser(User user) throws UsernameAlreadyInUseException;

	User findUserByUsername(String username);
	
}
