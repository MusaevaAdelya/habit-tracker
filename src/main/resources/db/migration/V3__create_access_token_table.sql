create table access_token (
    id bigserial not null,
    expired boolean not null,
    revoked boolean not null,
    token varchar(255) unique ,
    token_type varchar(255),
    user_id bigint,
    primary key (id),
    constraint FK_access_token_users
        foreign key (user_id)
            references users(id)
)