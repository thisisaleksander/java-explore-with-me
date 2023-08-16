CREATE TABLE IF NOT EXISTS stats
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    app     varchar(100) NOT NULL,
    uri     varchar(250) NOT NULL,
    ip      varchar(15) NOT NULL,
    created timestamp
);