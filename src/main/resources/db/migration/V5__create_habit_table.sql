create table habit(
			id integer not null,
			description varchar(255),
			end_date timestamp(6),
			goal integer,
			name varchar(255),
			periodicity integer,
			start_date timestamp(6),
			enable boolean not null,
            primary key (id)
)