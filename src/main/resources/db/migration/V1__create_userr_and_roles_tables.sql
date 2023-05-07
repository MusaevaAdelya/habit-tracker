create table roles (
                       id bigserial not null,
                       name varchar(255),
                       primary key (id)
);

create table user_roles (
                            user_id bigint not null,
                            role_id bigint not null
);

create table userr (
                       id bigserial not null,
                       email varchar(255),
                       password varchar(255),
                       enable boolean not null,
                       primary key (id)
);

alter table if exists user_roles
    add constraint FK_user_roles_roles
        foreign key (role_id)
            references roles;


alter table if exists user_roles
    add constraint FK_user_roles_userr
        foreign key (user_id)
            references userr;