-- create refresh_tokens
create table refresh_tokens
(
  id            varchar(40)  primary key,
  member_id     varchar(40)  not null,
  refresh_token varchar(128) not null unique,
  expired_at    bigint       not null,
  used_at       bigint,
  created_at    bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  updated_at    bigint       not null default extract(epoch from current_timestamp at time zone 'UTC'),
  versions      bigint       not null default 0,

  foreign key (member_id) references members (id)
);

create index idx_refresh_tokens_member_id on refresh_tokens (member_id);
