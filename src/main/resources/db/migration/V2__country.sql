create table if not exists country
(
    id           uuid          not null,
    alpha2code   varchar(2)    not null,
    alpha3code   varchar(3)    not null,
    full_name    varchar(1024) not null,
    numeric_code varchar(3)    not null,
    short_name   varchar(1024) not null,
    primary key (id)
);

create unique index country_alpha2code_idx on country (alpha2code);

create unique index country_alpha3code_idx on country (alpha3code);

create unique index country_fullName_idx on country (full_name);

create unique index country_numericCode_idx on country (numeric_code);

create unique index country_shortName_idx on country (short_name);

