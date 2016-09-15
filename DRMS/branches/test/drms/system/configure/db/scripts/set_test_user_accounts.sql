-- Create an admin login and password
delete from accounts.userrole where user_id = (select id from users where username='admin');
delete from accounts.users where username='admin';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'admin', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-admin@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='admin' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'Admin');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Create an operator login and password
delete from accounts.userrole where user_id = (select id from users where username='operator');
delete from accounts.users where username='operator';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'operator', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-operator@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='operator' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'Operator');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Create an operator login and password
delete from accounts.userrole where user_id = (select id from users where username='faciliy');
delete from accounts.users where username='faciliy';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'faciliy', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-faciliy@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='faciliy' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'FaciliyManager');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Create an operator login and password
delete from accounts.userrole where user_id = (select id from users where username='pss2ws');
delete from accounts.users where username='pss2ws';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'pss2ws', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-pss2ws@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='pss2ws' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'PSS2WS');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Set Gail's password
delete from accounts.users where username='gail';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'gail', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-gail@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='gail' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'Admin');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Set Ray's password
delete from accounts.users where username='ray';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'ray', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-ray@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='ray' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'Admin');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Set Gail's password
delete from accounts.users where username='richard';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'richard', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-richard@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='richard' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'Admin');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');



