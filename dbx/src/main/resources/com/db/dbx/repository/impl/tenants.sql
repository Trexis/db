create table Tenants (id identity,
						name varchar unique,
						description varchar,
						defaultappname varchar not null,
						primary key (id));
						