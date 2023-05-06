CREATE SEQUENCE seq_users start 1 1 increment 1;

CREATE TABLE userr
  (
     id         bigint NOT NULL default nextval('seq_users'),
     email       VARCHAR(255),
   	 password    VARCHAR(255),
     PRIMARY KEY (id)
  );

CREATE TABLE roles
  (
     id         bigint NOT NULL default nextval('seq_users'),
     name      VARCHAR(255),
    
     PRIMARY KEY (id)
  );