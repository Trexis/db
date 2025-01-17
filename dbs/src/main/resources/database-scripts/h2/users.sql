create table Users (id identity,
						username varchar not null,
						password varchar not null,
						firstName varchar not null, 
						lastName varchar not null,
						tenantname varchar,
						defaultappname varchar,
						role varchar not null,
						primary key (id));
create unique index UsersKey on Users(tenantname, username);

						
create table UserConnection (
  userId varchar(255) not null,
  providerId varchar(255) not null,
  providerUserId varchar(255),
  rank int not null,
  displayName varchar(255),
  profileUrl varchar(512),
  imageUrl varchar(512),
  accessToken varchar(1024) not null,
  secret varchar(255),
  refreshToken varchar(255),
  expireTime bigint,
  primary key (userId, providerId, providerUserId));
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);