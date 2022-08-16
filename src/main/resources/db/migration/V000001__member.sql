-- create member
create table member
(
  id         serial primary key,
  google_id  varchar(255) null,
  email      varchar(250) not null,
  password   varchar(100) not null,
  name       varchar(250) not null,
  created_at timestamp    not null default now(),
  updated_at timestamp    not null default now(),
  versions   bigint       not null default 0
);

create unique index member_IX1 on member (email);
create unique index member_IX2 on member (google_id);
