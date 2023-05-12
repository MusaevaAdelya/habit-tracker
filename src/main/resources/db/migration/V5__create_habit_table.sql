create table habit (
       id serial not null,
        completed_days integer not null,
        description varchar(255),
        enable boolean not null,
        end_date date,
        goal_days integer not null,
        goal_period_type smallint,
        habit_type smallint,
        name varchar(255),
        per_day integer not null,
        start_date date,
        user_id bigint,
        primary key (id),
        
        constraint FK_habit_users
                foreign key (user_id)
                    references users(id)
    )