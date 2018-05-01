DROP TABLE IF EXISTS products;
CREATE TABLE products
(
id SERIAL PRIMARY KEY,
name varchar(30),
description text,
default_price int,
default_currency varchar(3),
product_category int,
supplier int
);

DROP TABLE IF EXISTS suppliers;
CREATE TABLE suppliers
(
  id SERIAL PRIMARY KEY,
  name varchar(36),
  description text
);


DROP TABLE IF EXISTS product_categories;
CREATE TABLE product_categories
(
  id SERIAL PRIMARY KEY,
  name varchar(36),
  description text,
  department varchar(36)
);

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id SERIAL PRIMARY KEY,
  username varchar(36),
  email varchar(36),
  password text
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders
(
  id SERIAL PRIMARY KEY,
  user_id int references users(id),
  name varchar(36),
  email varchar(36),
  billing_address varchar(150),
  shipping_address varchar(150),
  phone varchar(46),
  creation_date timestamp,
  status varchar(36),
  shopping_cart_id int
);

DROP TABLE IF EXISTS shopping_carts;
CREATE TABLE shopping_carts
(
  id SERIAL PRIMARY KEY,
  creation_date timestamp,
  status varchar(36)
);

DROP TABLE IF EXISTS user_shopping_cart;
CREATE TABLE user_shopping_cart
(
  id SERIAL PRIMARY KEY,
  user_id int references users(id),
  shopping_cart_id int references  shopping_carts(id)
);

DROP TABLE IF EXISTS shopping_cart_product;
CREATE TABLE shopping_cart_product
(
  id SERIAL PRIMARY KEY,
  shopping_cart_id int references shopping_carts(id),
  product_id int references  products(id)
);