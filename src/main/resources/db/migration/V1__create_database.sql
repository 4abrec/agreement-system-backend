CREATE TABLE IF NOT EXISTS role
(
    "id"   serial      NOT NULL
        CONSTRAINT role_pk PRIMARY KEY,
    "name" varchar(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    "id"           serial       NOT NULL
        CONSTRAINT user_pk PRIMARY KEY,
    "username"     varchar(16)  NOT NULL,
    "password"     varchar(255) NOT NULL,
    "fio"          varchar(60)  NOT NULL,
    "university"   varchar(100) NULL,
    "group_number" varchar(30)  NULL,
    "address"      varchar(255) NULL,
    "position"     varchar(255) NULL,
    "phone_number" varchar(60)  NULL

);

CREATE TABLE IF NOT EXISTS user_role
(
    "user_id" int NOT NULL
        CONSTRAINT user_role_fk0 REFERENCES users,
    "role_id" int NOT NULL
        CONSTRAINT user_role_fk1 REFERENCES role,
    CONSTRAINT user_role_pk
        PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS module
(
    "id"    serial       NOT NULL
        CONSTRAINT module_pk PRIMARY KEY,
    "title" varchar(100) NOT NULL
);

CREATE TABLE user_module
(
    "user_id"   int NOT NULL
        CONSTRAINT user_module_fk0 REFERENCES users,
    "module_id" int NOT NULL
        CONSTRAINT user_module_fk1 REFERENCES module,
    CONSTRAINT user_module_pk
        PRIMARY KEY (user_id, module_id)
);

CREATE TABLE IF NOT EXISTS task
(
    "id"              serial       NOT NULL
        CONSTRAINT "task_pk" PRIMARY KEY,
    "title"           varchar(256) NOT NULL,
    "description"     TEXT         NOT NULL,
    "deadline"        timestamp    NOT NULL,
    "type_assessment" int          NOT NULL,
    "module_id"       int          NOT NULL
        CONSTRAINT task_fk0 REFERENCES module
);


CREATE TABLE IF NOT EXISTS solution
(
    "id"          serial    NOT NULL
        CONSTRAINT solution_pk PRIMARY KEY,
    "text"        TEXT      NULL,
    "mark"        int       NULL,
    "return_flag" bool      NULL,
    "datetime"    timestamp NULL,
    "status"      varchar   NULL,
    "author_id"   int       NOT NULL
        CONSTRAINT solution_fk0 REFERENCES users,
    "task_id"     int       NOT NULL
        CONSTRAINT solution_fk1 REFERENCES task
);

CREATE TABLE IF NOT EXISTS comment
(
    "id"          serial    NOT NULL
        CONSTRAINT comment_pk PRIMARY KEY,
    "text"        TEXT      NULL,
    "datetime"    timestamp NULL,
    "author_id"   int       NOT NULL
        CONSTRAINT comment_fk0 REFERENCES users,
    "solution_id" int       NOT NULL
        CONSTRAINT comment_fk1 REFERENCES solution
);

CREATE table message
(
    "id"        serial    NOT NULL
        CONSTRAINT message_pk PRIMARY KEY,
    "text"      TEXT      NOT NULL,
    "datetime"  timestamp NOT NULL,
    "author_id" int       NOT NULL
        CONSTRAINT message_fk0 REFERENCES users
);

CREATE table file
(
    "id"          varchar(255) NOT NULL
        CONSTRAINT file_pk PRIMARY KEY,
    "name"        varchar(100) NOT NULL,
    "type"        varchar(100) NOT NULL,
    "data"        oid          NOT NULL,
    "user_id"     int          NULL,
    "task_id"     int          NULL,
    "solution_id" int          NULL
        CONSTRAINT file_fk0 REFERENCES solution
);

