DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT(
  id INTEGER,
  description VARCHAR2(1000)
);

INSERT INTO PRODUCT (id, description) VALUES (1, 'product 1');
INSERT INTO PRODUCT (id, description) VALUES (2, 'product 2');
INSERT INTO PRODUCT (id, description) VALUES (1, 'product 3');
INSERT INTO PRODUCT (id, description) VALUES (4, 'product 4');
