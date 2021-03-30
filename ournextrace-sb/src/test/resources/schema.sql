CREATE SCHEMA IF NOT EXISTS OURNEXTRACE;

CREATE TABLE OURNEXTRACE.race_type (  id int(11) NOT NULL AUTO_INCREMENT,  name varchar(128) NOT NULL,  description varchar(256) DEFAULT NULL,  PRIMARY KEY (id));

CREATE TABLE OURNEXTRACE.race_race_type (  race_id int(11) NOT NULL, race_type_id int(11) NOT NULL,  PRIMARY KEY (race_id,race_type_id) );

CREATE TABLE OURNEXTRACE.user (
    id int(11) NOT NULL AUTO_INCREMENT,
    cred varchar(128) DEFAULT NULL,
    email varchar(256) NOT NULL,
    active int(11) NOT NULL DEFAULT '1',
    name varchar(256) NOT NULL,
    city varchar(45) DEFAULT NULL,
    state varchar(45) DEFAULT 'FL',
    country varchar(45) DEFAULT 'USA',
    first_name varchar(128) DEFAULT NULL,
    last_name varchar(256) DEFAULT NULL,
    zip varchar(45) DEFAULT NULL,
    last_updated datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id));

CREATE TABLE OURNEXTRACE.role( id int(11) NOT NULL, name varchar(45) NOT NULL, description varchar(256), PRIMARY KEY (id));

CREATE TABLE OURNEXTRACE.user_role( user_id int(11) NOT NULL,  role_id int(11) NOT NULL,  PRIMARY KEY (user_id,role_id));

CREATE TABLE OURNEXTRACE.races (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(128) NOT NULL,
  date datetime DEFAULT CURRENT_TIMESTAMP,
  description varchar(1024) NOT NULL,
  address_id int(11) DEFAULT '0',
  url varchar(512) DEFAULT NULL,
  public tinyint(4) NOT NULL DEFAULT '0',
  coupon_code varchar(1024) DEFAULT NULL,
  author_id int(11) NOT NULL DEFAULT '0',
  mod_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  active tinyint(4) DEFAULT '1',
  PRIMARY KEY (id)
);

CREATE TABLE OURNEXTRACE.address (
  id int(11) NOT NULL AUTO_INCREMENT,
  location varchar(128) NOT NULL,
  STREET varchar(128) DEFAULT NULL,
  CITY varchar(45) DEFAULT NULL,
  STATE varchar(45) NOT NULL DEFAULT 'FL',
  ZIP varchar(45) DEFAULT NULL,
  COUNTRY varchar(64) NOT NULL DEFAULT 'USA',
  PHONE varchar(45) DEFAULT NULL,
  NOTES varchar(1024) DEFAULT NULL,
  author_id int(11) NOT NULL DEFAULT '0',
  mod_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE OURNEXTRACE.my_race (
  race_id int(11) NOT NULL,
  user_id int(11) NOT NULL,
  my_race_status varchar(32) DEFAULT '0',
  is_paid tinyint(4) NOT NULL DEFAULT '1',
  cost decimal(10,0) DEFAULT '0',
  registration_date datetime DEFAULT CURRENT_TIMESTAMP,
  hotel_name varchar(128) DEFAULT NULL,
  notes varchar(1024) DEFAULT NULL,
  modified_date datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (race_id,user_id)
) COMMENT='My Event Meta Information for a single Event';

CREATE TABLE OURNEXTRACE.my_race_race_type (
  user_id int(11) NOT NULL,
  race_id int(11) NOT NULL,
  race_type_id int(11) NOT NULL,
  PRIMARY KEY (user_id,race_id,race_type_id)
) ;

CREATE TABLE OURNEXTRACE.forgot_password (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  token varchar(1024) DEFAULT NULL,
  expiry_date datetime DEFAULT NULL,
  PRIMARY KEY (id)
)
