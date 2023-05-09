create table users (
    id bigserial not null,
    email varchar(255) not null unique ,
    enable boolean not null,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    password varchar(255),
    points bigint,
    profile_url varchar(255),
    role varchar(255),
    primary key (id)
);
