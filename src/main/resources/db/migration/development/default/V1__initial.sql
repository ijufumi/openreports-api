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


-- create t_member
create table t_member (
  member_id serial primary key,
  email_address varchar(250) not null,
  password varchar(100) not null,
  name varchar(250) not null,
  is_admin char not null default '0',
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

create unique index member_IX1 on t_member(email_address);

-- create t_group
create table t_group (
  group_id serial primary key,
  group_name varchar(250) not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create r_member_group
create table r_member_group (
  member_id integer references t_member(member_id),
  group_id integer references t_group(group_id),
  primary key (member_id, group_id)
);

-- create t_function
create table t_function (
  function_id integer primary key,
  function_name varchar(250) not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create r_group_function
create table r_group_function (
  group_id integer references t_group(group_id),
  function_id integer references t_function(function_id),
  primary key (group_id, function_id)
);

-- create t_report_template
create table t_report_template (
  template_id serial primary key,
  file_name varchar(250) not null,
  fild_path varchar(250) not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

create unique index report_template_IX1 on t_report_template(file_name);

-- create t_report
create table t_report (
  report_id serial primary key,
  report_name varchar(250) not null,
  template_id integer references t_report_template(template_id),
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create t_scheduled_report
create table t_scheduled_report (
  scheduled_id serial primary key,
  report_id integer not null,
  cron_expression varchar(250) not null,
  params bytea,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create t_report_group
create table t_report_group (
  report_group_id serial primary key,
  report_group_name varchar(250) not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create r_report_report_group
create table r_report_report_group (
  report_id integer references t_report(report_id),
  report_group_id integer references t_report_group(report_group_id),
  primary key (report_id, report_group_id)
);

-- create r_group_report_group
create table r_group_report_group (
  group_id integer references t_group(group_id),
  report_group_id integer references t_report_group(report_group_id),
  primary key (group_id, report_group_id)
);

-- create t_report_param
create table t_report_param (
  param_id serial primary key,
  param_key varchar(250) not null,
  param_name varchar(250) not null,
  param_values text not null,
  description varchar(500) not null,
  param_type char not null default '0',
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

-- create t_report_param_config
create table t_report_param_config (
  config_id serial primary key,
  report_id integer references t_report(report_id),
  param_id integer references t_report_param(param_id),
  page_no integer not null default 0,
  seq integer not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  versions bigint not null default 0
);

create index report_param_config_IX1 on t_report_param_config(report_id);

--
-- test data
--

insert into t_group (group_name, created_at, updated_at) values ('admin', now(), now());
insert into t_group (group_name, created_at, updated_at) values ('assistant', now(), now());

insert into t_member (email_address, password, name, is_admin, created_at, updated_at) values ('ijufumi@gmail.com', 'a11efa8c78f2870fd3918ee6528fd8bcb2daa9edb9f7a57384ac7a40c98a8daf', 'administrator', '1', now(), now());
insert into r_member_group values (1, 1);
insert into r_member_group values (1, 2);

insert into t_function (function_id, function_name, created_at, updated_at) values (1, 'テスト機能1', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (2, 'テスト機能2', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (3, 'テスト機能3', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (4, 'テスト機能4', now(), now());
insert into t_function (function_id, function_name, created_at, updated_at) values (5, 'テスト機能5', now(), now());
insert into r_group_function values (1, 1);
insert into r_group_function values (1, 2);
insert into r_group_function values (2, 3);
insert into r_group_function values (2, 4);
insert into r_group_function values (2, 5);

insert into t_report_template(file_name, fild_path, created_at, updated_at) values ('sample1.xlsx', 'sample1.xlsx', now(), now());
insert into t_report_template(file_name, fild_path, created_at, updated_at) values ('sample2.xlsx', 'sample2.xlsx', now(), now());
insert into t_report_template(file_name, fild_path, created_at, updated_at) values ('sample3.xlsx', 'sample3.xlsx', now(), now());
insert into t_report_template(file_name, fild_path, created_at, updated_at) values ('sample4.xlsx', 'sample4.xlsx', now(), now());

insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート1', 1, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート2', 2, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート3', 3, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート4', 4, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート5', 1, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート6', 2, now(), now());
insert into t_report (report_name, template_id, created_at, updated_at) values ('テストレポート7', 3, now(), now());

insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ1', now(), now());
insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ2', now(), now());
insert into t_report_group (report_group_name, created_at, updated_at) values ('テストグループ3', now(), now());

insert into r_report_report_group values (1, 1);
insert into r_report_report_group values (1, 2);
insert into r_report_report_group values (1, 3);
insert into r_report_report_group values (2, 1);
insert into r_report_report_group values (2, 2);
insert into r_report_report_group values (2, 3);
insert into r_report_report_group values (3, 1);
insert into r_report_report_group values (3, 2);
insert into r_report_report_group values (3, 3);
insert into r_report_report_group values (4, 3);
insert into r_report_report_group values (5, 3);
insert into r_report_report_group values (6, 3);
insert into r_report_report_group values (7, 1);

insert into r_group_report_group values (1, 1);
insert into r_group_report_group values (1, 2);
insert into r_group_report_group values (1, 3);

insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param1', 'ぱらむ1', 'テスト用パラメメータ1', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param2', 'ぱらむ2', 'テスト用パラメメータ2', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param3', 'ぱらむ3', 'テスト用パラメメータ3', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param4', 'ぱらむ4', 'テスト用パラメメータ4', '1', '');
insert into t_report_param (param_key, param_name, description, param_type, param_values) values ('param5', 'ぱらむ5', 'テスト用パラメメータ5', '1', '');

insert into t_report_param_config (report_id, param_id) values (1, 1);
insert into t_report_param_config (report_id, param_id) values (1, 2);
insert into t_report_param_config (report_id, param_id) values (1, 3);
insert into t_report_param_config (report_id, param_id) values (1, 4);
insert into t_report_param_config (report_id, param_id) values (1, 5);
