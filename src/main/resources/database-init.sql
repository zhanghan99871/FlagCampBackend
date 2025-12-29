DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS customers;

CREATE TABLE users
(
    id         SERIAL PRIMARY KEY   NOT NULL,
    email      TEXT UNIQUE          NOT NULL,
    password   TEXT                 NOT NULL,
    username   TEXT
);




