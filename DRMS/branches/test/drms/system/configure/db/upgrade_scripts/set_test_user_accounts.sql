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
delete from accounts.userrole where user_id = (select id from users where username='Faciliy');
delete from accounts.users where username='Faciliy';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'Faciliy', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-Faciliy@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='Faciliy' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'FaciliyManager');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

-- Create an operator login and password
delete from accounts.userrole where user_id = (select id from users where username='pss2ws');
delete from accounts.users where username='pss2ws';
insert into accounts.users (domainname, username, password, email, status) values ('PSS2', 'pss2ws', '14fb4e3c7b7f1954ed246e33a170f206', 'noop-pss2ws@akuacom.com', 1);
set @user_id = (select id from accounts.users where username='pss2ws' limit 1);
insert into accounts.userrole (user_id, rolename) values (@user_id, 'PSS2WS');
insert into accounts.userrole (user_id, rolename) values (@user_id, 'website');

