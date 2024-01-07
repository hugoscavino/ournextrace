------ ADDRESS
DROP TABLE IF EXISTS address;
CREATE TABLE address (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     location TEXT NOT NULL,
                                     street TEXT,
                                     city TEXT,
                                     state TEXT DEFAULT 'FL' NOT NULL,
                                     zip TEXT,
                                     country TEXT DEFAULT 'USA' NOT NULL,
                                     phone TEXT,
                                     notes TEXT,
                                     author_id INTEGER DEFAULT 0 NOT NULL,
                                     mod_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


CREATE TABLE my_race (
                                     race_id INTEGER NOT NULL,
                                     user_id INTEGER NOT NULL,
                                     my_race_status TEXT,
                                     paid boolean DEFAULT true NOT NULL,
                                     cost numeric(10,0) DEFAULT 0,
                                     registration_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                     hotel_name TEXT,
                                     notes TEXT,
                                     modified_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- MY_RACE_RACE_TYPE
CREATE TABLE my_race_race_type (
                                               user_id integer NOT NULL,
                                               race_id integer NOT NULL,
                                               race_type_id integer NOT NULL
);

-- RACE_RACE_TYPE
CREATE TABLE race_race_type (
                                            race_id integer NOT NULL,
                                            race_type_id integer NOT NULL
);


-- RACE TYPE
CREATE TABLE race_type (
                                       id integer NOT NULL,
                                       name TEXT NOT NULL,
                                       description TEXT NOT NULL,
                                       short_desc TEXT NOT NULL
);

-- RACES
CREATE TABLE races (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                description TEXT NOT NULL,
                                address_id INTEGER DEFAULT 0 NOT NULL,
                                url TEXT,
                                public boolean DEFAULT false NOT NULL,
                                coupon_code TEXT,
                                author_id INTEGER DEFAULT 0 NOT NULL,
                                mod_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                active boolean DEFAULT true NOT NULL,
                                cancelled boolean DEFAULT false NOT NULL
);


-- ROLE
CREATE TABLE role (
                                  id integer NOT NULL,
                                  name character varying(45) NOT NULL,
                                  description character varying(256)
);


-- USER
CREATE TABLE "user" (
                                    id integer NOT NULL,
                                    cred TEXT,
                                    email TEXT,
                                    active boolean DEFAULT true NOT NULL,
                                    name TEXT NOT NULL,
                                    city TEXT,
                                    state TEXT,
                                    country TEXT DEFAULT 'USA' NOT NULL,
                                    first_name TEXT,
                                    last_name TEXT,
                                    zip TEXT,
                                    social_provider TEXT DEFAULT 'IJUDY' NOT NULL,
                                    last_login timestamp without time zone,
                                    last_updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


-- USER-ROLE
CREATE TABLE user_role (
                                       user_id integer NOT NULL,
                                       role_id integer NOT NULL
);
