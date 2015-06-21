create table Components (id identity,
						tenantname varchar not null,
						appname varchar not null,
						pagename varchar not null,
						name varchar not null,
						title varchar,
						primary key (id));
create unique index ComponentsKey on Components(tenantname, appname, pagename, name);


create table Preferences(id identity,
						tenantname varchar not null,
						appname varchar not null,
						itemtype varchar not null,
						itemname varchar not null,
						prefname varchar not null,
						prefvalue varchar
						);
create unique index PreferencesKey on Preferences(tenantname, appname, itemtype, itemname);
