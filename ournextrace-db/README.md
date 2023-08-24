# Setting Up liquibase project 
Using an external liquibase.properties file for all the credentials

Scripts assume a superuser has already created the schema for the
database and liquibase. This is so not to add the postgres superuser
credentials anywhere in these scripts

### create users with secure password
    ournextrace_user
    ournextraceadmin

### create schema
    ournextrace owner is ournextraceadmin

    -- DROP SCHEMA IF EXISTS ournextrace;
    CREATE SCHEMA IF NOT EXISTS ournextrace AUTHORIZATION ournextraceadmin;

### liquibase.properties example in src/main/resources

    changelogFile: ./db/changelog/changelog-master.xml
    driver: org.postgresql.Driver
    url: jdbc:postgresql://server:port/<database-name>
    classpath: /../postgresql-X.X.X.jar
    usern@me: <pg admin usr name>
    pa$$w0rd: <pg admin credentials>

### Maven command

    mvn liquibase:update

### Manually Create Three Users

Need at least one ADMIN (3)

    INSERT INTO ournextrace."user"(
    id, cred, email, active, name, city, state, country, first_name, last_name, zip, social_provider, last_login, last_updated)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);


    INSERT INTO ournextrace."user"(
    id, cred, email, active, name, city, state, country,
    first_name, last_name, zip)
    VALUES (3, '', 'admin@ijudy.com', true, 'admin', 'city', 'FL', 'USA', 'FIRST', 'LAST', '33544');

    INSERT INTO ournextrace."user"(
    id, cred, email, active, name, city, state, country,
    first_name, last_name, zip)
    VALUES (0, '', 'anonymous@ijudy.com', true, 'anonymous', 'city', 'FL', 'USA', 'FIRST', 'LAST', '33544');

### Remote PostgreSQL
Update roles so that Java client can login

    ALTER ROLE "asunotest" WITH LOGIN;