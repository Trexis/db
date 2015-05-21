create table Components (id identity,
						tenantname varchar not null,
						appname varchar not null,
						pagename varchar not null,
						reference varchar not null,
						content varchar,
						primary key (id));
