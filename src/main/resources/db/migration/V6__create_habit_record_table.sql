 create table habit_record (
       id serial not null,
        date date,
        habit_id integer,
        primary key (id),
        
        constraint FK_habit_record 
       foreign key (habit_id) 
       references habit
    )