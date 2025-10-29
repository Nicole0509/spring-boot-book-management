create table users
(
    id         serial
        constraint users_pk
            primary key,
    username   varchar(255)                        not null,
    email      varchar(255)                        not null
        constraint users_unique_email_constraint
            unique,
    password   varchar(255)                        not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp
);

