create table Apps (id identity,
						tenantname varchar not null,
						name varchar not null,
						description varchar,
						url varchar not null,
						allowannoymous int,
						primary key (id));						
create unique index AppsKey on Apps(tenantname, name);