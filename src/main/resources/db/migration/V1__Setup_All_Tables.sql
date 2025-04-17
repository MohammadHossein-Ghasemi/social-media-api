CREATE TABLE user_ (
    id BIGINT NOT NULL PRIMARY KEY,
    username VARCHAR(225) NOT NULL UNIQUE ,
    email VARCHAR(225) NOT NULL UNIQUE ,
    password VARCHAR(225) NOT NULL,
    bio TEXT,
    profile_picture_url VARCHAR(225),
    created_date TIMESTAMP NOT NULL ,
    updated_date TIMESTAMP NOT NULL
);

CREATE SEQUENCE user_seq start with 1 increment by 1;

CREATE TABLE followers (
    follower_id BIGINT NOT NULL ,
    following_id BIGINT NOT NULL ,
    PRIMARY KEY (follower_id,following_id),
    FOREIGN KEY (following_id) REFERENCES user_(id),
    FOREIGN KEY (follower_id) REFERENCES user_(id)
);

CREATE TABLE following (
    follower_id BIGINT NOT NULL ,
    following_id BIGINT NOT NULL ,
    PRIMARY KEY (follower_id,following_id),
    FOREIGN KEY (following_id) REFERENCES user_(id),
    FOREIGN KEY (follower_id) REFERENCES user_(id)
);

CREATE TABLE post (
    id BIGINT NOT NULL PRIMARY KEY ,
    content TEXT NOT NULL ,
    image_url VARCHAR(255) ,
    created_date TIMESTAMP NOT NULL ,
    updated_date TIMESTAMP NOT NULL ,
    user_id BIGINT NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE SEQUENCE post_seq start with 1 increment by 1;

CREATE TABLE user_like (
    id BIGINT NOT NULL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    post_id BIGINT NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES user_(id),
    FOREIGN KEY (post_id) REFERENCES post(id)
);

CREATE SEQUENCE like_seq start with 1 increment by 1;

CREATE TABLE notification (
    id BIGINT NOT NULL  PRIMARY KEY ,
    message TEXT NOT NULL ,
    is_read BOOLEAN NOT NULL DEFAULT false,
    created_date TIMESTAMP NOT NULL ,
    user_id BIGINT NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE SEQUENCE notification_seq start with 1 increment by 1;

CREATE TABLE comment (
    id BIGINT NOT NULL PRIMARY KEY ,
    content TEXT NOT NULL ,
    created_date TIMESTAMP NOT NULL ,
    post_id BIGINT NOT NULL ,
    user_id BIGINT NOT NULL ,
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE SEQUENCE comment_seq start with 1 increment by 1;
