create table clients
(
    id   bigserial not null primary key,
    name varchar(50) not null
);

create table addresses
(
    id        bigserial not null primary key,
    street    varchar(255) not null,
    client_id bigint not null references clients(id)
);

create table phones
(
    id        bigserial not null primary key,
    number    varchar(50) not null,
    client_id bigint not null references clients(id)
);