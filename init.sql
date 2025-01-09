CREATE DATABASE practice_rest;
use practice_rest;

CREATE TABLE product (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE category (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE product_category (
                                  product_id INT,
                                  category_id INT,
                                  PRIMARY KEY (product_id, category_id),
                                  FOREIGN KEY (product_id) REFERENCES product(id) ,
                                  FOREIGN KEY (category_id) REFERENCES category(id)
);