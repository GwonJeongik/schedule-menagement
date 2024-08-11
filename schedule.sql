create table requestScheduleDto
(
    schedule_id       varchar(36),
    schedule_password varchar(10),
    task              varchar(20),
    admin_name        varchar(10),
    registration_date datetime,
    modification_date datetime,
    primary key (schedule_id)
);

create table admin
(
    admin_id varchar(36),
    admin_name varchar(10),
    email varchar(50),
    registration_date Date,
    modification_date Date,
    primary key (admin_id)
);
