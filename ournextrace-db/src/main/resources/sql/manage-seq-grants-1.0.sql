SELECT pg_catalog.setval('ournextrace.forgot_password_id_seq', 100, false);
SELECT pg_catalog.setval('ournextrace.address_id_seq', 200, false);
SELECT pg_catalog.setval('ournextrace.race_type_id_seq', 300, false);
SELECT pg_catalog.setval('ournextrace.races_id_seq', 200, true);
SELECT pg_catalog.setval('ournextrace.user_id_seq', 110, true);

ALTER TABLE ONLY ournextrace.address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace.forgot_password
    ADD CONSTRAINT forgot_password_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace.my_race
    ADD CONSTRAINT my_race_pkey PRIMARY KEY (race_id, user_id);

ALTER TABLE ONLY ournextrace.my_race_race_type
    ADD CONSTRAINT my_race_race_type_pkey PRIMARY KEY (user_id, race_id, race_type_id);

ALTER TABLE ONLY ournextrace.race_race_type
    ADD CONSTRAINT race_race_type_pkey PRIMARY KEY (race_id, race_type_id);

ALTER TABLE ONLY ournextrace.race_type
    ADD CONSTRAINT race_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace.races
    ADD CONSTRAINT races_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY ournextrace.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.address TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.forgot_password TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.my_race TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.my_race_race_type TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.race_race_type TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.race_type TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.races TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace."role" TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace."user" TO ournextrace_user;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE ournextrace.user_role TO ournextrace_user;

GRANT ALL ON SEQUENCE ournextrace.forgot_password_id_seq TO ournextrace_user;
GRANT ALL ON SEQUENCE ournextrace.address_id_seq TO ournextrace_user;
GRANT ALL ON SEQUENCE ournextrace.races_id_seq TO ournextrace_user;
GRANT ALL ON SEQUENCE ournextrace.race_type_id_seq TO ournextrace_user;
GRANT ALL ON SEQUENCE ournextrace.user_id_seq TO ournextrace_user;
