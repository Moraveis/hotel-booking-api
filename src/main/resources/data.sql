SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE room;
TRUNCATE TABLE reservation;
SET REFERENTIAL_INTEGRITY TRUE;
ALTER TABLE room ALTER COLUMN id RESTART WITH 1;
ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;

INSERT INTO room(id, number, suite, available) VALUES(1, '001', false, true);