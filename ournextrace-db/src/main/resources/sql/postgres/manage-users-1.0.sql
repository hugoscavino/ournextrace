------------------ USER ----------
GRANT CONNECT ON DATABASE ijudy TO ournextrace_user;
GRANT USAGE ON SCHEMA ournextrace TO ournextrace_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA ournextrace TO ournextrace_user;
GRANT USAGE, SELECT  ON ALL SEQUENCES IN SCHEMA ournextrace TO ournextrace_user;

------------------ ADMIN ----------
GRANT ALL PRIVILEGES ON DATABASE ijudy TO ournextraceadmin;
GRANT USAGE ON SCHEMA ournextrace TO ournextraceadmin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA ournextrace TO ournextraceadmin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA ournextrace TO ournextraceadmin;
