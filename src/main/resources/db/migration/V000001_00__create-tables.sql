-- create members
create table members
(
  id         varchar(40) primary key,
  google_id  varchar(255) null,
  email      varchar(250) not null,
  password   varchar(100) not null,
  name       varchar(250) not null,
  created_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions   bigint       not null default 0
);

create unique index member_UIX1 on members (email);
create unique index member_UIX2 on members (google_id);


-- create workspaces
create table workspaces
(
  id         varchar(40) primary key,
  name       varchar(250) not null,
  slug       varchar(250) not null,
  created_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions   bigint       not null default 0
);

-- create roles
create table roles
(
  id         varchar(40) primary key,
  role_type  varchar(250) not null,
  created_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions   bigint       not null default 0
);

create unique index role_UIX1 on roles (role_type);

-- create functions
create table functions
(
  id         varchar(40) primary key,
  resource   varchar(100) not null,
  action     varchar(100) not null,
  created_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions   bigint       not null default 0
);

create unique index functions_UIX1 on functions (resource, action);

-- create role_functions
create table role_functions
(
  id          varchar(40) primary key,
  role_id     varchar(40) not null,
  function_id varchar(40) not null,
  created_at  bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at  bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions    bigint      not null default 0,

  foreign key (role_id) references roles (id),
  foreign key (function_id) references functions (id)
);

create unique index role_functions_UIX1 on role_functions (role_id, function_id);

-- create workspace_members
create table workspace_members
(
  workspace_id varchar(40) not null,
  member_id    varchar(40) not null,
  role_id      varchar(40) not null,
  created_at   bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at   bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions     bigint      not null default 0,

  primary key (workspace_id, member_id),
  foreign key (workspace_id) references workspaces (id),
  foreign key (member_id) references members (id),
  foreign key (role_id) references roles (id)
);

-- create driver_types
create table driver_types
(
  id                varchar(40) primary key,
  name              varchar(250) not null,
  jdbc_driver_class varchar(250) not null,
  created_at        bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at        bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions          bigint       not null default 0
);

-- create data_sources
create table data_sources
(
  id             varchar(40) primary key,
  name           varchar(250) not null,
  url            varchar(250) not null,
  username       varchar(250) not null,
  password       varchar(250) not null,
  driver_type_id varchar(40)  not null,
  workspace_id   varchar(40)  not null,
  created_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions       bigint       not null default 0,

  foreign key (driver_type_id) references driver_types (id),
  foreign key (workspace_id) references workspaces (id)
);

-- create report_templates
create table report_templates
(
  id           varchar(40) primary key,
  name         varchar(250) not null,
  file_path    varchar(250) not null,
  workspace_id varchar(40)  not null,
  storage_type varchar(20)  not null default 'local',
  file_size    bigint       not null default 0,
  is_seed      tinyint      not null default 0,
  created_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions     bigint       not null default 0,

  foreign key (workspace_id) references workspaces (id)
);

-- create reports
create table reports
(
  id             varchar(40) primary key,
  name           varchar(250) not null,
  report_template_id    varchar(40)  not null,
  workspace_id   varchar(40)  not null,
  data_source_id varchar(40),
  created_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions       bigint       not null default 0,

  foreign key (report_template_id) references report_templates (id),
  foreign key (data_source_id) references data_sources (id),
  foreign key (workspace_id) references workspaces (id)
);

-- create report_groups
create table report_groups
(
  id           varchar(40) primary key,
  name         varchar(255) not null,
  workspace_id varchar(40)  not null,
  created_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions     bigint       not null default 0,

  foreign key (workspace_id) references workspaces (id)
);

-- create report_group_reports
create table report_group_reports
(
  id              varchar(40) primary key,
  report_id       varchar(40) not null,
  report_group_id varchar(40) not null,
  created_at      bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at      bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions        bigint      not null default 0,

  foreign key (report_id) references reports (id),
  foreign key (report_group_id) references report_groups (id)
);

-- create report_parameters
create table report_parameters
(
  id             varchar(40) primary key,
  workspace_id   varchar(40)  not null,
  parameter_type varchar(100) not null,
  value          varchar(255) null,
  created_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at     bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions       bigint       not null default 0,

  foreign key (workspace_id) references workspaces (id)
);

-- create report_report_parameters
create table report_report_parameters
(
  id                  varchar(40) primary key,
  report_id           varchar(40) not null,
  report_parameter_id varchar(40) not null,
  created_at          bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at          bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions            bigint      not null default 0,

  foreign key (report_id) references reports (id),
  foreign key (report_parameter_id) references report_parameters (id)
);

-- create storages
create table storages_s3
(
  id                    varchar(40) primary key,
  workspace_id          varchar(40) not null,
  aws_access_key_id     varchar(40),
  aws_secret_access_key varchar(40),
  aws_region            varchar(40),
  s3_bucket_name        varchar(40),
  created_at            bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at            bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions              bigint      not null default 0,

  foreign key (workspace_id) references workspaces (id)
);
