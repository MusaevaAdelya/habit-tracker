create table if not exists password_reset_token (
                                      token_id bigserial not null,
                                      expiration_time timestamp(6),
                                      token varchar(255),
                                      user_id bigint,
                                      primary key (token_id)
);

alter table if exists password_reset_token
    add constraint FK_password_reset_token_userr
        foreign key (user_id)
            references userr;