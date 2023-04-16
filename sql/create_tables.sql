CREATE TABLE IF NOT EXISTS products (
  id SERIAL NOT NULL,
  product_name varchar(250) NOT NULL,
  product_price int,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS product (
  product_id SERIAL NOT NULL,
  product_name varchar(250) NOT NULL,
  product_price int,
  PRIMARY KEY (product_id)
);