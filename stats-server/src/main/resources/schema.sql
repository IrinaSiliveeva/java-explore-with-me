DROP TABLE IF EXISTS stats;
CREATE TABLE IF NOT EXISTS stats
(
    ID        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        CONSTRAINT stats_pk PRIMARY KEY,
    APP       varchar(500),
    URI       varchar(500),
    IP        varchar(500),
    TIMESTAMP timestamp
);