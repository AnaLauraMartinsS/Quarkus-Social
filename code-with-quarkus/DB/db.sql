CREATE DATABASE social;

CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      age INT NOT NULL
);

SELECT * FROM User;

CREATE TABLE Posts (
                       id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                       post_text VARCHAR(150) NOT NULL,
                       dateTime TIMESTAMP NOT NULL,
                       user_id BIGINT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES User(id)
);

SELECT * FROM Posts;

create table Followers (
                           id bigint not null auto_increment primary key,
                           user_id bigint not null references User(id),
                           follower_id bigint not null references User(id)
);