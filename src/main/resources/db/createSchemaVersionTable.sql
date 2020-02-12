create table if not exists changelog (
  change_number bigserial primary key not null,
  complete_dt timestamp with time zone not null,
  applied_by text not null,
  description text not null
);

