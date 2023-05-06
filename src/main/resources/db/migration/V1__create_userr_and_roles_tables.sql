-- CREATE SEQUENCE seq_users start 1 1 increment 1;
--
-- CREATE TABLE userr
--   (
--      id         bigint NOT NULL default nextval('seq_users'),
--      email       VARCHAR(255),
--    	 password    VARCHAR(255),
--      PRIMARY KEY (id)
--   );
--
-- CREATE TABLE roles
--   (
--      id         bigint NOT NULL default nextval('seq_users'),
--      name      VARCHAR(255),
--
--      PRIMARY KEY (id)
--   );

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