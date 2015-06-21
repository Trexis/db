create table Assets(id identity,
						tenantname varchar not null,
						appname varchar not null,
						category varchar not null,
						path varchar not null,
						filename varchar not null,
						mimetype varchar not null,
						checksum varchar not null
						);
create unique index AssetsKey on Assets(tenantname, appname, category, path, filename);
