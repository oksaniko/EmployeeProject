create table kafka_errors (
id varchar(32) not null, 
topic_name varchar(255) not null,
message varchar(255) not null, 
error_txt varchar(255) not null, 
error_create_date timestamp(6) not null, 
error_last_sent_date timestamp(6) not null,
primary key (id));