INSERT INTO role VALUES (1, 'ROLE_ADMIN');
INSERT INTO role VALUES (2, 'ROLE_STUDENT');

SELECT setval('role_id_seq', (select max(id) from role));