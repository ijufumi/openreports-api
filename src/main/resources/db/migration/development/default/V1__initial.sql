-- create session
-- H2 Database compatible
create table skinny_sessions (
  id bigserial not null primary key,
  created_at timestamp not null,
  expire_at timestamp not null
);
create table servlet_sessions (
  jsession_id varchar(100) not null primary key,
  skinny_session_id bigint not null,
  created_at timestamp not null,
  foreign key(skinny_session_id) references skinny_sessions(id)
);
create table skinny_session_attributes (
  skinny_session_id bigint not null,
  attribute_name varchar(128) not null,
  attribute_value bytea,
  foreign key(skinny_session_id) references skinny_sessions(id)
);
alter table skinny_session_attributes add constraint
  skinny_session_attributes_unique_idx
  unique(skinny_session_id, attribute_name);


-- create member
create table member (
  member_id serial primary key,
  email_address varchar(250) not null,
  password varchar(100) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

create unique index member_IX1 on member(email_address);

-- create group
create table group (
  group_id serial primary key,
  group_name varchar(250) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

-- create member_group_rel
create table member_group_rel (
  member_id integer primary key,
  group_id integer primary key
);

-- create report
create table report (
  report_id serial primary key,
  report_name varchar(250) not null,
  template_path varchar(250) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

-- create scheduled_report
create table scheduled_report (
  scheduled_id serial primary key,
  report_id bigint not null,
  cron_expression varchar(250) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

insert into group (name, password, created_at) values ('admin', now(), now());
insert into member (email_address, password, created_at) values ('ijufumi@gmail.com', 'password', now(), now());
insert into member_group_rel values (1, 1);