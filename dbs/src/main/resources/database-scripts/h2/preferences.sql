create table Preferences(id identity,
						tenantname varchar not null,
						appname varchar not null,
						itemtype varchar not null,
						itemname varchar not null,
						prefname varchar not null,
						prefvalue varchar
						);
create unique index PreferencesKey on Preferences(tenantname, appname, itemtype, itemname);
