create table employees (
id varchar(32) not null, 
name varchar(255) not null, 
position varchar(20) not null check (position in ('MANAGER','EMPLOYEE','UNDEFINED','TECH')), 
status varchar(20) not null check (status in ('WORKING','TRIAL','TIME_OFF','DISMISSED','DELETED')), 
primary key (id));