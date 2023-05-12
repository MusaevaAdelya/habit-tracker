create table confirmation_token (
    token_id bigserial not null,
    confirmation_token varchar(255),
    created_date timestamp(6),
    user_id bigint not null,
    primary key (token_id),
    constraint FK_confirmation_token_users
        foreign key (user_id)
            references users(id)
);

