CREATE TABLE users
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    name              VARCHAR(20)  NOT NULL,
    role              VARCHAR(10)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    created_date_time DATETIME(6) NOT NULL,
    updated_date_time DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);
