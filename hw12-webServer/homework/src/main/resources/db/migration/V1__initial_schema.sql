create table admins
(
    id         bigserial not null primary key,
    login      varchar(50),
    password   varchar(50)
);

create table clients
(
    id         bigserial not null primary key,
    name       varchar(50),
    address_id bigint
);

create table phones
(
    id        bigserial not null primary key,
    number    varchar(50),
    client_id bigint
);

create table addresses
(
    id     bigserial not null primary key,
    street varchar(255)
);