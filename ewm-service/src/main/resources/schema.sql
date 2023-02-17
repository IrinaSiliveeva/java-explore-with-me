DROP TABLE IF EXISTS users, categories, compilations, events, compilation_event, requests,comments;
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY not null,
    name  varchar(255)                            not null,
    email varchar(512) unique                     not null,
    CONSTRAINT PK_USER PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar(255) unique                     not null,
    CONSTRAINT CATEGORIES_PK PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  varchar(255),
    pinned boolean,
    CONSTRAINT COMPILATION_PK PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         varchar(2000)                           not null,
    category_id        bigint,
    description        varchar(7000)                           not null,
    title              varchar(1000)                           not null,
    event_date         timestamp with time zone,
    paid               boolean                                 not null,
    lat                real,
    lon                real,
    initiator_id       bigint,
    created_on         timestamp with time zone,
    participant_limit  int,
    published_on       timestamp with time zone,
    request_moderation boolean,
    state              varchar(20)                             not null,
    CONSTRAINT EVENTS_PK PRIMARY KEY (id),
    CONSTRAINT EVENTS_CATEGORIES_ID_FK FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT EVENTS_USERS_ID_FK FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilation_event
(
    event_id       bigint,
    compilation_id bigint,
    PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT fk_compiled_events_compilations FOREIGN KEY (compilation_id)
        REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compiled_events_events FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      timestamp with time zone,
    event_id     bigint                                  not null,
    requester_id bigint                                  not null,
    status       varchar(20)                             not null,
    CONSTRAINT REQUESTS_PK PRIMARY KEY (id),
    CONSTRAINT REQUESTS_EVENTS_ID_FK FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT REQUESTS_USERS_ID_FK FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    author_id bigint                                  not null,
    event_id  bigint                                  not null,
    text      varchar                                 not null,
    created   timestamp                               not null,
    CONSTRAINT COMMENTS_PK PRIMARY KEY (id),
    CONSTRAINT COMMENTS_USERS_ID_FK FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT COMMENTS_EVENTS_ID_FK FOREIGN KEY (event_id) REFERENCES events (id) on delete cascade
);