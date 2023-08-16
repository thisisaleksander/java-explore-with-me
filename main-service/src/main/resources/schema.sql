CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    email   varchar(254) NOT NULL,
    name    varchar(250) NOT NULL,
    UNIQUE  (email)
);

CREATE TABLE IF NOT EXISTS locations
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    lat     FLOAT NOT NULL,
    lon     FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name    varchar(50),
    UNIQUE  (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    annotation         varchar(2000),
    category_id        BIGINT,
    created_on         timestamp,
    description        varchar(7000),
    event_date         timestamp,
    initiator_id       BIGINT,
    location_id        BIGINT,
    paid               boolean,
    participant_limit  INT NOT NULL,
    published_on       timestamp,
    request_moderation boolean,
    state              varchar(50),
    title              varchar(120),
    views              BIGINT,
    confirmed_requests INT,
    CONSTRAINT  fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
    CONSTRAINT  fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
    CONSTRAINT  fk_events_to_locations FOREIGN KEY(location_id) REFERENCES locations(id),
    UNIQUE      (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    created           timestamp,
    event_id          BIGINT,
    requester_id      BIGINT,
    status            varchar(100),
    CONSTRAINT  fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT  fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
    UNIQUE      (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    event_id    BIGINT,
    pinned      boolean,
    title       varchar(200),

    CONSTRAINT  fk_compilations_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    UNIQUE      (id)
);

CREATE TABLE IF NOT EXISTS compilations_to_events
(
    compilation_id  BIGINT,
    event_id        BIGINT
);

CREATE TABLE IF NOT EXISTS comments
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    created           timestamp,
    updated           timestamp,
    event_id          BIGINT,
    commentator_id    BIGINT,
    status            varchar(100),
    text              varchar(2000),
    CONSTRAINT  fk_comments_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT  fk_comments_to_users FOREIGN KEY(commentator_id) REFERENCES users(id),
    UNIQUE      (id)
);