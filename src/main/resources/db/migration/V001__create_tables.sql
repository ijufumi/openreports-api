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
                                 file_path varchar(250) not null,
                                 created_at timestamp not null default now(),
                                 updated_at timestamp not null default now(),
                                 versions bigint not null default 0
);

create unique index report_template_IX1 on t_report_template(file_name);

-- create t_report_template_history
create table t_report_template_history (
                                         history_id serial primary key,
                                         template_id integer not null,
                                         file_name varchar(250) not null,
                                         file_path varchar(250) not null,
                                         created_at timestamp not null default now(),
                                         updated_at timestamp not null default now(),
                                         versions bigint not null default 0
);

create index report_template_history_IX1 on t_report_template_history(template_id);
create index report_template_history_IX2 on t_report_template_history(file_name);

-- create t_report
create table t_report (
                        report_id serial primary key,
                        report_name varchar(250) not null,
                        description varchar(250) not null,
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

