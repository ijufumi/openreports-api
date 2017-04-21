create table member (
  member_id bigint not null PRIMARY key,
  email_address VARCHAR(250) not null,
  password varchar(100) not null,
  created_at timestamp not null
);