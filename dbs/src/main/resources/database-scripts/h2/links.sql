create table Links (id identity,
						tenantname varchar not null,
						appname varchar not null,
						parentlinkname varchar,
						name varchar not null,
						url varchar not null,
						pagename varchar not null,
						primary key (id));
create unique index LinksKey on Links(tenantname, appname, parentlinkname, name);