# Setting Up flyway project 

### Maven command

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

