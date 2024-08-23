create table outbox
(
    id            int auto_increment primary key,
    aggregatetype varchar(255) not null,
    aggregateid   varchar(255) not null,
    type          varchar(255) not null,
    payload       json         null
);
