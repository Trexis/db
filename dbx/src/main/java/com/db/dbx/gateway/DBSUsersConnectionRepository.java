package com.db.dbx.gateway;

import java.util.List;
import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

public class DBSUsersConnectionRepository implements UsersConnectionRepository {

	public DBSUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator){
		
	}
	
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds) {
		// TODO Auto-generated method stub
		return null;
	}

	public ConnectionRepository createConnectionRepository(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
