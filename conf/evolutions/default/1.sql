# Users schema

# --- !Ups

CREATE TABLE User (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  eMail VARCHAR (256) NOT NULL,

  PRIMARY KEY (id),
  UNIQUE KEY (name)
);

# --- !Downs

DROP TABLE User;