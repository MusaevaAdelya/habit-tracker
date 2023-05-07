create table confirmation_token (
                                    token_id bigserial not null,
                                    confirmation_token varchar(255),
                                    created_date timestamp(6),
                                    id bigint not null,
                                    primary key (token_id)
);


alter table if exists confirmation_token
    add constraint FK_confirmation_token_userr
        foreign key (id)
            references userr;

