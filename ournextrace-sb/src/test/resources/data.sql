-- ADDRESS
-- NO ADDRESS ADDRESS
INSERT INTO OURNEXTRACE.address
(id,location,STREET,CITY,STATE,ZIP,COUNTRY,PHONE,NOTES,author_id,mod_date)
VALUES
(0,'No Location','No Street','No City','XX','XXXXX','USA',
'No Phone','No Notes',0, {ts '2019-01-01 01:00:00'});

INSERT INTO OURNEXTRACE.address
(id,location,STREET,CITY,STATE,ZIP,COUNTRY,PHONE,NOTES,author_id,mod_date)
VALUES
(2,'Main Beach - Amelia Island','32 N Fletcher Ave,','Amelia Island','FL','32034','USA',
'(813) 555-1212','The Location has limited parking',0, {ts '2019-01-23 22:45:34'});


-- RACE
INSERT INTO OURNEXTRACE.races
(id,name,date,description,address_id,url,public,coupon_code,author_id,mod_date,active)
VALUES
(1,'Zooma Florida Half Marathon 13.1', {ts '2018-10-18 00:00:00'},
'Zooma Florida was voted one of the top 20 half marathons AND top 20 for beautiful, on-course scenery',
2,'http://zoomarun.com/race/florida/', 1, NULL, 0, {ts '2019-01-23 22:44:59'}, 1);

INSERT INTO OURNEXTRACE.races (id, name, date, description,address_id, url, public, coupon_code, author_id, mod_date, active)
VALUES(
201, 'Event at Al Lopez', {ts '2019-01-28 00:00:00'},
'This is a great event',
0, 'http://wwww/google.com', 1, NULL, 11, {ts '2019-01-27 11:34:55'}, 1);

-- MY RACE
INSERT INTO OURNEXTRACE.my_race
(race_id, user_id, my_race_status, is_paid, cost,registration_date,
hotel_name, notes, modified_date) VALUES
(1, 1, 'GOING', 1, 100, {ts '2019-03-19 00:00:00'},
 'Hotel Name for ID 1-1', '<p>NOte</p>', {ts '2019-03-18 23:31:43'} );

INSERT INTO OURNEXTRACE.my_race
(race_id, user_id, my_race_status, is_paid, cost,registration_date,
hotel_name, notes, modified_date) VALUES (
201, 1, 'GOING', 0, 123, {ts '2019-04-16 03:18:56'},
'Hotel Name for ID 201-1', '<p>rrr</p>', {ts '2019-04-15 23:19:26'});

-- USER
INSERT INTO OURNEXTRACE.user
(id,cred,email,active,name,city,state,country,first_name,last_name,zip,last_updated)
VALUES( 1, 'wt9fcMxuNS', 'hugo@scavino.org', 1, 'Hugo Scavino', 'Tampa', 'FL', 'USA', 'Hugo', 'Scavino', '33710',
{ts '2019-01-15 20:31:44'});

-- RACE TYPES
INSERT INTO OURNEXTRACE.race_type(id,name,description) VALUES (1,'Full Marathon 26.2', '26.2 Miles');
INSERT INTO OURNEXTRACE.race_type(id,name,description) VALUES (2,'Half Marathon 13.1', '13.1 Miles');
INSERT INTO OURNEXTRACE.race_type(id,name,description) VALUES (40, '5K', '5K - 3.1 Mile Run');
INSERT INTO OURNEXTRACE.race_type(id,name,description) VALUES (50, '1 Mile', '1 Mile Run');

-- RACE <--> RACE TYPE
INSERT INTO OURNEXTRACE.race_race_type(race_id,race_type_id) VALUES(60,90);
INSERT INTO OURNEXTRACE.race_race_type(race_id,race_type_id) VALUES(60,100);

-- ROLE
INSERT INTO OURNEXTRACE.role(id,name,description) VALUES(1,'USER','Authenticated UserEntity');
INSERT INTO OURNEXTRACE.role(id,name,description) VALUES(2,'POWER_USER','Power UserEntity');
INSERT INTO OURNEXTRACE.role(id,name,description) VALUES(3,'ADMIN','Administrator');
