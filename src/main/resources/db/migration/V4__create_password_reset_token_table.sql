create table if not exists password_reset_token (
    token_id bigserial not null,
    expiration_time timestamp(6),
    token varchar(255),
    user_id bigint,
    primary key (token_id),
    constraint FK_password_token_users
        foreign key (user_id)
            references users(id)
);