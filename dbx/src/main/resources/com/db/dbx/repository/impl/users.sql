create table Users (id identity,
						username varchar unique,
						password varchar not null,
						firstName varchar not null, 
						lastName varchar not null,
						tenantname varchar not null,
						defaultappname varchar not null,
						primary key (id));
