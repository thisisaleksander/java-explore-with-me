create table if not exists users (
    id bigint generated always as identity not null,
    email varchar(254) not null,
    name varchar(250) not null,
    constraint unique_user_email unique (email),
    constraint pk_users primary key (id)
);

create table if not exists locations (
    id bigint generated always as identity not null,
    lat float not null,
    lon float not null,
    constraint pk_locations primary key (id)
);

create table if not exists categories (
    id bigint generated always as identity not null,
    name varchar(50),
    constraint unique_category_name unique (name),
    constraint pk_categories primary key (id)
);

create table if not exists events (
    id bigint generated always as identity not null,
    annotation varchar(2000),
    category_id bigint,
    created_on timestamp,
    description varchar(7000),
    event_date timestamp,
    initiator_id bigint,
    location_id bigint,
    paid boolean,
    participant_limit int not null,
    published_on timestamp,
    request_moderation boolean,
    state varchar(50),
    title varchar(120),
    views bigint,
    confirmed_requests int,
    constraint pk_events primary key (id),
    constraint fk_events_to_categories foreign key (category_id) references categories(id),
    constraint fk_events_to_users foreign key (initiator_id) references users(id),
    constraint fk_events_to_locations foreign key (location_id) references locations(id),
    constraint unique_event_id unique (id)
);

create table if not exists compilations (
    id bigint generated always as identity not null,
    event_id bigint,
    pinned boolean,
    title varchar(200),
    constraint pk_complications primary key (id),
    constraint fk_compilations_to_events foreign key (event_id) references events (id),
    constraint unique_compilation_id unique (id)
);

create table if not exists compilations_to_events (
    compilation_id bigint,
    event_id bigint
);

create table if not exists requests (
    id bigint generated always as identity not null,
    created timestamp,
    event_id bigint,
    requester_id bigint,
    status varchar(100),
    constraint pk_requests primary key (id),
    constraint fk_requests_to_events foreign key (event_id) references events(id),
    constraint fk_requests_to_users foreign key (requester_id) references users(id),
    constraint unique_request_id unique (id)
);