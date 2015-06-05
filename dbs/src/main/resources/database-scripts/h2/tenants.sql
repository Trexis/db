create table Tenants (id identity,
						name varchar,
						description varchar,
						primary key (id));
create unique index TenantsKey on Tenants(name);		