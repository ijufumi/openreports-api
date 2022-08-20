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

  primary key(workspace_id, member_id),
  foreign key (workspace_id) references workspaces(id),
  foreign key (member_id) references members(id)
);
