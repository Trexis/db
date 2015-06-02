create table Pages (id identity,
						tenantname varchar not null,
						appname varchar,
						name varchar not null,
						title varchar not null,
						isapplicationpage int not null,
						primary key (id));
