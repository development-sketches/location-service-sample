create table if not exists country_event
(
    id        uuid         not null,
    version   int          not null,
    type      varchar(32)  not null,
    timestamp timestamp    not null,
    user_id   uuid         null,
    user_name varchar(255) null,
    json      text,
    primary key (id, version)
);

create index country_event_type_idx on country_event (type);

create index country_event_timestamp_idx on country_event (timestamp);

create index country_event_user_id_idx on country_event (user_id);

create index country_event_user_name_idx on country_event (user_name);
