-- ROLES
INSERT INTO role(id,name,description) VALUES(1,'USER','Authenticated UserEntity');
INSERT INTO role(id,name,description) VALUES(2,'POWER_USER','Power UserEntity');
INSERT INTO role(id,name,description) VALUES(3,'ADMIN','Administrator');
INSERT INTO role(id,name,description) VALUES(4,'SYSTEM','System Account');

-- Default user for system inserts of races
INSERT INTO user_role (user_id, role_id) VALUES (0, 4);

-- User to Roles
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO user_role (user_id, role_id) VALUES (1, 3);
