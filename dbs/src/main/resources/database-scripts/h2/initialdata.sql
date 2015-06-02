insert into Users (tenantname, username, password, firstName, lastName, defaultappname, role) values (null, 'admin', 'admin', 'Admin', 'User', null, 'ROLE_ADMIN');

insert into Apps (tenantname, name, description, url, allowannoymous) values ('demobank', 'default', 'Demo Bank Public', 'demobank.db.com', 1);
insert into Apps (tenantname, name, description, url, allowannoymous) values ('demobank', 'siteb', 'Demo Bank Secure', 'demobank.dbcc.com', 0);

insert into Users (tenantname, username, password, firstName, lastName, defaultappname, role) values ('demobank', 'hendrikt', 'hendrikt', 'Hendrik', 'Tredoux', 'default', 'ROLE_USER');
insert into Users (tenantname, username, password, firstName, lastName, defaultappname, role) values ('demobank', 'herberr', 'herberr', 'Herber', 'de Ruijter', 'default', 'ROLE_USER');
insert into Users (tenantname, username, password, firstName, lastName, defaultappname, role) values ('demobank', 'jonw', 'jonw', 'Jonathan', 'Wright', 'siteb', 'ROLE_USER');

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'default', 'app_401', 'Demo Bank Public', 1);
insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'default', 'app_404', 'Demo Bank Public', 1);
insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'siteb', 'app_401', 'Demo Bank Secure', 1);
insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'siteb', 'app_404', 'Demo Bank Secure', 1);

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'default', 'index', 'Demo Bank Public', 0);
insert into Components(tenantname, appname, pagename, name) values ('demobank', 'default', 'index', '1');
insert into Links (tenantname, appname, name, url, pagename) values ('demobank', 'default', 'home', '/', 'index');

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('demobank', 'default', 'about', 'Demo Bank Public - About Us', 0);
insert into Components(tenantname, appname, pagename, name) values ('demobank', 'default', 'about', '2');
insert into Links (tenantname, appname, name, url, pagename) values ('demobank', 'default', 'about', '/aboutus', 'about');

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('bank1', 'siteb', 'dashboard', 'Demo Bank Dashboard', 0);
insert into Components(tenantname, appname, pagename, name) values ('demobank', 'siteb', 'dashboard', '3');
insert into Links (tenantname, appname, name, url, pagename) values ('demobank', 'siteb', 'dashboard', '/', 'dashboard');


insert into Tenants (name, description, defaultappname) values ('bankx', 'Bank X', 'default');
insert into Apps (tenantname, name, description, url, allowannoymous) values ('bankx', 'default', 'Bank X Online Banking', 'ec2-52-24-170-42.us-west-2.compute.amazonaws.com', 1);
insert into Users (tenantname, username, password, firstName, lastName, defaultappname, role) values ('bankx', 'bankuser', 'bankuser', 'Bank', 'User', 'default', 'ROLE_USER');
insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('bankx', '', '401', 'Bank X Online Banking', 0);
insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('bankx', '', '404', 'Bank X Online Banking', 0);

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('bankx', 'default', 'index', 'Bank X Online Banking', 0);
insert into Components(tenantname, appname, pagename, name) values ('bankx', 'default', 'index', '1');
insert into Links (tenantname, appname, name, url, pagename) values ('bankx', 'default', 'home', '/', 'index');

insert into Pages (tenantname, appname, name, title, isapplicationpage) values ('bankx', 'default', 'dashboard', 'Bank X Online Banking - Dashboard', 0);
insert into Components(tenantname, appname, pagename, name) values ('bankx', 'default', 'dashboard', '1');
insert into Links (tenantname, appname, name, url, pagename) values ('bankx', 'default', 'dashboard', '/dashboard', 'about');
