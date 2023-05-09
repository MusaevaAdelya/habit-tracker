create table habit(
			id integer not null,
			description varchar(255),
			end_date timestamp(6),
			goal integer,
			name varchar(255),
			periodicity integer,
			start_date timestamp(6),
			enable boolean not null,
			user_id bigint,
            primary key (id),
            constraint FK_habit_users
                foreign key (user_id)
                    references users(id)
)