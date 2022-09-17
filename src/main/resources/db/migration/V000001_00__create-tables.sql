-- create members
create table members
(
  id         varchar(40) primary key,
  google_id  varchar(255) null,
  email      varchar(250) not null,
  password   varchar(100) not null,
  name       varchar(250) not null,
  created_at timestamp    not null default now(),
  updated_at timestamp    not null default now(),
  versions   bigint       not null default 0
);

create unique index member_IX1 on members (email);
create unique index member_IX2 on members (google_id);


-- create workspaces
create table workspaces
(
  id         varchar(40) primary key,
  name       varchar(250) not null,
  created_at timestamp    not null default now(),
  updated_at timestamp    not null default now(),
  versions   bigint       not null default 0
);

-- create workspace_members
create table workspace_members
(
  workspace_id varchar(40),
  member_id    varchar(40),
  created_at   timestamp not null default now(),
  updated_at   timestamp not null default now(),
  versions     bigint    not null default 0,

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
  created_at        timestamp    not null default now(),
  updated_at        timestamp    not null default now(),
  versions          bigint       not null default 0
)

-- create data_sources
create table data_sources
(
  id             varchar(40) primary key,
  name           varchar(250) not null,
  url            varchar(250) not null,
  driver_type_id varchar(40),
  created_at     timestamp    not null default now(),
  updated_at     timestamp    not null default now(),
  versions       bigint       not null default 0,

  foreign key (driver_type_id) references driver_types (id),
)

-- create reports
create table reports
(
  id                 varchar(40) primary key,
  name               varchar(250) not null,
  report_template_id varchar(40)  not null,
  data_source_id     varchar(40)  not null,
  created_at         timestamp    not null default now(),
  updated_at         timestamp    not null default now(),
  versions           bigint       not null default 0,

  foreign key (report_template_id) references report_templates (id),
  foreign key (data_source_id) references data_sources (id),
)

-- create report_templates
create table report_templates
(
  id         varchar(40) primary key,
  name       varchar(250) not null,
  file_path  varchar(250) not null,
  created_at timestamp    not null default now(),
  updated_at timestamp    not null default now(),
  versions   bigint       not null default 0
)
