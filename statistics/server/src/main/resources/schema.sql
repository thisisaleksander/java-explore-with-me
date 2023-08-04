create table if not exists Statistics (
    id bigint generated always as identity not null primary key,
    app varchar(100) not null,
    uri varchar(250) not null,
    ip varchar(15) not null,
    time_added timestamp
);