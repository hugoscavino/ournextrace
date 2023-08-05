-- ROLES
INSERT INTO ournextrace.role(id,name,description) VALUES(1,'USER','Authenticated UserEntity');
INSERT INTO ournextrace.role(id,name,description) VALUES(2,'POWER_USER','Power UserEntity');
INSERT INTO ournextrace.role(id,name,description) VALUES(3,'ADMIN','Administrator');
INSERT INTO ournextrace.role(id,name,description) VALUES(4,'SYSTEM','System Account');

-- Default user for system inserts of races
INSERT INTO ournextrace.user_role VALUES (0, 4);
INSERT INTO ournextrace.user_role VALUES (3, 1);
INSERT INTO ournextrace.user_role VALUES (3, 2);
INSERT INTO ournextrace.user_role VALUES (3, 3);

--"id"	"cred"	"email"	"active"	"name"	"city"	"state"	"country"	"first_name"	"last_name"	"zip"	"social_provider"	"last_login"	"last_updated"
--0		"hugo@scavino.org"	true	"Hugo Scavino"	"Wesley Chapel"	"FL"	"USA"				"ijudy.com"		"2023-08-05 15:16:38.116577"