DROP TABLE IF EXISTS books,authors;

CREATE TABLE books(
id SERIAL PRIMARY KEY,
author VARCHAR(250) NOT NULL,
title VARCHAR(250) NOT NULL,
priceOld VARCHAR(250) DEFAULT NULL,
price VARCHAR(250) DEFAULT NULL
);


create table authors (
                         id INT,
                         first_name VARCHAR(50),
                         last_name VARCHAR(50)
);