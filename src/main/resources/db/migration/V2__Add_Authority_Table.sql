CREATE TABLE authority(
    id BIGINT PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    name VARCHAR(225) NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES user_(id)
);

CREATE SEQUENCE authority_seq start with 1 increment by 1;