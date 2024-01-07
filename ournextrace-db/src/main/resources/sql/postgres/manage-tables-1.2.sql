------ ADDRESS
DROP TABLE IF EXISTS ournextrace.address;
CREATE TABLE ournextrace.address (
    id integer NOT NULL,
    location character varying(128) NOT NULL,
    street character varying(128) DEFAULT NULL::character varying,
    city character varying(45) DEFAULT NULL::character varying,
    state character varying(45) DEFAULT 'FL'::character varying,
    zip character varying(45) DEFAULT NULL::character varying,
    country character varying(64) DEFAULT 'USA'::character varying,
    phone character varying(45) DEFAULT NULL::character varying,
    notes character varying(1024) DEFAULT NULL::character varying,
    author_id integer DEFAULT 0 NOT NULL,
    mod_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);
------ ADDRESS OWNER
ALTER TABLE ournextrace.address OWNER TO ournextraceadmin;

------ ADDRESS SEQ
CREATE SEQUENCE ournextrace.address_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
------ ADDRESS SEQ OWNER
ALTER TABLE ournextrace.address_id_seq OWNER TO ournextraceadmin;

CREATE TABLE ournextrace.my_race (
    race_id integer NOT NULL,
    user_id integer NOT NULL,
    my_race_status character varying(32) DEFAULT '0'::character varying,
    paid boolean DEFAULT true NOT NULL,
    cost numeric(10,0) DEFAULT '0'::numeric,
    registration_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    hotel_name character varying(128) DEFAULT NULL::character varying,
    notes character varying(1024) DEFAULT NULL::character varying,
    modified_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE ournextrace.my_race OWNER TO ournextraceadmin;

-- MY_RACE_RACE_TYPE
CREATE TABLE ournextrace.my_race_race_type (
    user_id integer NOT NULL,
    race_id integer NOT NULL,
    race_type_id integer NOT NULL
);

-- MY_RACE_RACE_TYPE OWNER
ALTER TABLE ournextrace.my_race_race_type OWNER TO ournextraceadmin;

-- RACE_RACE_TYPE
CREATE TABLE ournextrace.race_race_type (
    race_id integer NOT NULL,
    race_type_id integer NOT NULL
);

-- RACE_RACE_TYPE OWNER
ALTER TABLE ournextrace.race_race_type OWNER TO ournextraceadmin;

-- RACE TYPE
CREATE TABLE ournextrace.race_type (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(256) DEFAULT NULL::character varying NOT NULL,
    short_desc character varying NOT NULL
);

-- RACE TYPE OWNER
ALTER TABLE ournextrace.race_type OWNER TO ournextraceadmin;
COMMENT ON COLUMN ournextrace.race_type.short_desc IS 'used for UI where real estate is expensive';

-- RACE TYPE SEQ
CREATE SEQUENCE ournextrace.race_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- RACE TYPE SEQ OWNER
ALTER TABLE ournextrace.race_type_id_seq OWNER TO ournextraceadmin;
ALTER SEQUENCE ournextrace.race_type_id_seq OWNED BY ournextrace.race_type.id;

-- RACES
CREATE TABLE ournextrace.races (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    description character varying(1024) NOT NULL,
    address_id integer NOT NULL DEFAULT 0,
    url character varying(512) DEFAULT NULL::character varying,
    public boolean DEFAULT false NOT NULL,
    coupon_code character varying(1024) DEFAULT NULL::character varying,
    author_id integer NOT NULL DEFAULT 0,
    mod_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    active boolean DEFAULT true NOT NULL,
    cancelled boolean DEFAULT false NOT NULL
);

-- RACES OWNER
ALTER TABLE ournextrace.races OWNER TO ournextraceadmin;

-- RACE SEQ
CREATE SEQUENCE ournextrace.races_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- RACE SEQ OWNER
ALTER TABLE ournextrace.races_id_seq OWNER TO ournextraceadmin;
ALTER SEQUENCE ournextrace.races_id_seq OWNED BY ournextrace.races.id;

-- ROLE
CREATE TABLE ournextrace.role (
    id integer NOT NULL,
    name character varying(45) NOT NULL,
    description character varying(256)
);
-- ROLE OWNER
ALTER TABLE ournextrace.role OWNER TO ournextraceadmin;

-- USER
CREATE TABLE ournextrace."user" (
    id integer NOT NULL,
    cred character varying(128) DEFAULT NULL::character varying,
    email character varying(256) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    name character varying(256) NOT NULL,
    city character varying(45) DEFAULT NULL::character varying,
    state character varying(45) DEFAULT 'FL'::character varying,
    country character varying(45) DEFAULT 'United States'::character varying,
    first_name character varying(128) DEFAULT NULL::character varying,
    last_name character varying(256) DEFAULT NULL::character varying,
    zip character varying(45) DEFAULT NULL::character varying,
    social_provider character varying(45) DEFAULT 'ijudy.com'::character varying,
    last_login timestamp without time zone,
    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

-- USER OWNER
ALTER TABLE ournextrace."user" OWNER TO ournextraceadmin;

-- USER SEQ
CREATE SEQUENCE ournextrace.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
-- USER SEQ OWNER
ALTER TABLE ournextrace.user_id_seq OWNER TO ournextraceadmin;
ALTER SEQUENCE ournextrace.user_id_seq OWNED BY ournextrace."user".id;

-- USER-ROLE
CREATE TABLE ournextrace.user_role (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);

-- FORGOT PASSWORD
CREATE TABLE ournextrace.forgot_password
(
    id integer NOT NULL,
    user_id integer NOT NULL,
    token character varying(1024) NOT NULL,
    expiry_date timestamp without time zone NOT NULL
);
ALTER TABLE ournextrace.forgot_password OWNER TO ournextraceadmin;
------ FORGOT PASSWORD SEQ
CREATE SEQUENCE ournextrace.forgot_password_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
------ ADDRESS SEQ OWNER
ALTER TABLE ournextrace.forgot_password_id_seq OWNER TO ournextraceadmin;

-- USER-ROLE OWNER
ALTER TABLE ournextrace.user_role OWNER TO ournextraceadmin;

-- ADDRESS DEFAULT
ALTER TABLE ONLY ournextrace.address ALTER COLUMN id SET DEFAULT nextval('ournextrace.address_id_seq'::regclass);
-- race_type DEFAULT
ALTER TABLE ONLY ournextrace.race_type ALTER COLUMN id SET DEFAULT nextval('ournextrace.race_type_id_seq'::regclass);
-- RACES DEFAULT
ALTER TABLE ONLY ournextrace.races ALTER COLUMN id SET DEFAULT nextval('ournextrace.races_id_seq'::regclass);
-- USER DEFAULT
ALTER TABLE ONLY ournextrace."user" ALTER COLUMN id SET DEFAULT nextval('ournextrace.user_id_seq'::regclass);
-- ADDRESS DEFAULT
ALTER TABLE ONLY ournextrace.forgot_password ALTER COLUMN id SET DEFAULT nextval('ournextrace.forgot_password_id_seq'::regclass);
