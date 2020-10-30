# twofactorauth
Two Factor Auth App build with Spring Boot, Spring Security, and Websockets

## How it works?

A user logs in using a username and password in the application. Once authenticated, the app will send an SMS/Email (not implemented yet) to the user that contains a link to verify his login.
Meanwhile, he will be holding up on a page and can't go forward in the application.
Once the user clicks on the link sent in SMS/Email, the login page refreshes itself and the user lands on the home page.

## Setup:

### DB Schema:

```
drop table if exists authorities;
drop table if exists two_factor_auth;
drop table if exists users;

create table users (
    username varchar_ignorecase(50) not null primary key,
    password varchar_ignorecase(500) not null,
    enabled boolean not null
);

create table authorities (
    username varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);

create table two_factor_auth (
    id bigint not null primary key,
    auth_token varchar(255),
    date_created date,
    date_updated date,
    is_verified boolean,
    username varchar(255),
    constraint fk_two_factor_auth_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username,authority);
create unique index ix_two_factor_auth_username on two_factor_auth (auth_token,username);

create sequence hibernate_sequence;
```

### Insert a user:

```
insert into users values ('example@email.com', '$2a$10$MIcKGI2Vg1DRoxmhC8mzVuwES0CUhVVF7xVQg7vY6dGPsvMLsQmSC', 1);
insert into authorities values ('example@email.com', 'USER_ROLE');
```


Make sure we set our personal email in the insert query above.
And, set the gmail username/password in the application.properties spring.mail.username, spring.mail.password

Application can be accessed at http://localhost:8080
Login with username (your email) and password (pass)
