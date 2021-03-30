DROP SCHEMA ournextrace CASCADE;
CREATE SCHEMA ournextrace;
GRANT ALL ON SCHEMA ournextrace TO postgres;
GRANT ALL ON SCHEMA ournextrace TO public;
COMMENT ON SCHEMA ournextrace IS 'standard ournextrace schema for ournextrace.com online calendar';

ALTER SCHEMA ournextrace OWNER TO ournextraceadmin;