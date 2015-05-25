insert into Tenants (name, description, defaultappname) values ('bank1', 'Tenant 1 Bank example 1', 'default');
insert into Tenants (name, description, defaultappname) values ('bank2', 'Tenant 2 Bank example 2', 'default');

insert into Apps (name, description, tenantname, url, allowannoymous) values ('default', 'Bank1 Digital Bank A', 'bank1', 'bank1.db.com', 1);
insert into Apps (name, description, tenantname, url, allowannoymous) values ('default', 'Bank2 Retail', 'bank2', 'bank2.db.com', 1);
insert into Apps (name, description, tenantname, url, allowannoymous) values ('siteb', 'Bank2 Commercial', 'bank2', 'bank2.dbcc.com', 0);


insert into Users (username, password, firstName, lastName, tenantname, defaultappname) values ('hendrikt', 'hendrikt', 'Hendrik', 'Tredoux', 'bank1', 'default');
insert into Users (username, password, firstName, lastName, tenantname, defaultappname) values ('herberr', 'herberr', 'Herber', 'de Ruijter', 'bank2', 'default');
insert into Users (username, password, firstName, lastName, tenantname, defaultappname) values ('jonw', 'jonw', 'Jonathan', 'Wright', 'bank2', 'siteb');



insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', '', '401', 'Bank 1 Digital Bank A', '<html><head></head><body>No Access</body></html>', 0);
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', '', '404', 'Bank 1 Digital Bank A', '<html><head></head><body>Not Found</body></html>', 0);


insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', 'default', 'app_401', 'Bank 1 Digital Bank A', '<html><head></head><body>Access Denied</body></html>', 1);
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', 'default', 'app_404', 'Bank 1 Digital Bank A', '<html><head></head><body>Page not found</body></html>', 1);

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', 'default', 'index', 'Bank 1 Digital Bank A', '<html><head></head><body>Home Page<div data-comp-id="1"></div></body></html>', 0);
insert into Components(tenantname, appname, pagename, reference, content) values ('bank1', 'default', 'index', '1', '<div>Home Page Hello world</div>');
insert into Links (tenantname, appname, name, url, pagename) values ('bank1', 'default', 'home', '/', 'index');

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank1', 'default', 'about', 'Bank 1 Digital Bank A', '<html><head></head><body>About Us<div data-comp-id="1"></div></body></html>', 0);
insert into Components(tenantname, appname, pagename, reference, content) values ('bank1', 'default', 'about', '1', '<div>About Us</div>');
insert into Links (tenantname, appname, name, url, pagename) values ('bank1', 'default', 'about', '/aboutus', 'about');

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank2', 'default', 'app_401', 'Bank 1 Digital Bank A', '<html><head></head><body>Access Denied</body></html>', 1);
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank2', 'default', 'app_404', 'Bank 1 Digital Bank A', '<html><head></head><body>Page not found</body></html>', 1);

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank2', 'siteb', 'app_401', 'Bank 1 Digital Bank A', '<html><head></head><body>Access Denied</body></html>', 1);
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank2', 'siteb', 'app_404', 'Bank 1 Digital Bank A', '<html><head></head><body>Page not found</body></html>', 1);

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank2', 'siteb', 'dashboard', 'Bank 2 Dashboard', '<html><head></head><body>Dashboard<div data-comp-id="1"></div></body></html>', 0);
insert into Components(tenantname, appname, pagename, reference, content) values ('bank2', 'siteb', 'dashboard', '1', '<div>Dashboard content</div>');
insert into Links (tenantname, appname, name, url, pagename) values ('bank2', 'siteb', 'dashboard', '/', 'about');


insert into Tenants (name, description, defaultappname) values ('bank3', 'Tenant 3 Bank example 2', 'default');
insert into Apps (name, description, tenantname, url, allowannoymous) values ('default', 'Bank3 Digital Bank A', 'bank3', 'ec2-52-24-170-42.us-west-2.compute.amazonaws.com', 1);
insert into Users (username, password, firstName, lastName, tenantname, defaultappname) values ('bankuser', 'bankuser', 'Bank', 'User', 'bank3', 'default');
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank3', '', '401', 'Bank 3 Digital Bank A', '<html><head></head><body>No Access</body></html>', 0);
insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank3', '', '404', 'Bank 3 Digital Bank A', '<html><head></head><body>Not Found</body></html>', 0);

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank3', 'default', 'index', 'Bank 3 Digital Bank A', '<html><head></head><body>Home Page<div data-comp-id="1"></div></body></html>', 0);
insert into Components(tenantname, appname, pagename, reference, content) values ('bank3', 'default', 'index', '1', '<div>Home Page Hello world</div>');
insert into Links (tenantname, appname, name, url, pagename) values ('bank3', 'default', 'home', '/', 'index');

insert into Pages (tenantname, appname, name, title, content, isapplicationpage) values ('bank3', 'default', 'about', 'Bank 3 Digital Bank A', '<html><head></head><body>About Us<div data-comp-id="1"></div></body></html>', 0);
insert into Components(tenantname, appname, pagename, reference, content) values ('bank3', 'default', 'about', '1', '<div>About Us</div>');
insert into Links (tenantname, appname, name, url, pagename) values ('bank3', 'default', 'about', '/aboutus', 'about');
