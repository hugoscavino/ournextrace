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

### liquibase.properties example in src/main/resources

    changelogFile: ./db/changelog/changelog-master.xml
    driver: org.postgresql.Driver
    url: jdbc:postgresql://server:port/<database-name>
    classpath: /../postgresql-X.X.X.jar
    usern@me: <pg admin usr name>
    pa$$w0rd: <pg admin credentials>

