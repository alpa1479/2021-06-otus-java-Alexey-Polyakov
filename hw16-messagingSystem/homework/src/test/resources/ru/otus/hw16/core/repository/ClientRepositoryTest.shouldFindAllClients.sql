insert into clients (id, name) values  (1, 'test-name-1');
insert into phones (id, client_id, number) values (1, 1, 'test-number-1-1');
insert into phones (id, client_id, number) values (2, 1, 'test-number-1-2');
insert into phones (id, client_id, number) values (3, 1, 'test-number-1-3');
insert into addresses (id, client_id, street) values (1, 1, 'test-address-1');

insert into clients (id, name) values  (2, 'test-name-2');
insert into phones (id, client_id, number) values (4, 2, 'test-number-2');
insert into addresses (id, client_id, street) values (2, 2, 'test-address-2');

insert into clients (id, name) values  (3, 'test-name-3');
insert into phones (id, client_id, number) values (5, 3, 'test-number-3');
insert into addresses (id, client_id, street) values (3, 3, 'test-address-3');