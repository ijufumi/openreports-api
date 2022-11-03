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

create unique index member_IX1 on members (email);
create unique index member_IX2 on members (google_id);


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

-- create workspace_members
create table workspace_members
(
  workspace_id varchar(40) not null,
  member_id    varchar(40) not null,
  created_at   bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at   bigint      not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions     bigint      not null default 0,

  primary key (workspace_id, member_id),
  foreign key (workspace_id) references workspaces (id),
  foreign key (member_id) references members (id)
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
  created_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at   bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions     bigint       not null default 0,

  foreign key (workspace_id) references workspaces (id)
);

-- create reports
create table reports
(
  id                 varchar(40) primary key,
  name               varchar(250) not null,
  report_template_id varchar(40)  not null,
  workspace_id       varchar(40)  not null,
  data_source_id     varchar(40)  not null,
  created_at         bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at         bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions           bigint       not null default 0,

  foreign key (report_template_id) references report_templates (id),
  foreign key (data_source_id) references data_sources (id),
  foreign key (workspace_id) references workspaces (id)
);

-- create storages
create table storages
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
