# --- !Ups
CREATE TABLE prime_entity (
  value INT NOT NULL PRIMARY KEY,
  is_prime BIT NOT NULL,
  sub_primes VARCHAR(MAX) NOT NULL
  );


# --- !Downs
DROP TABLE prime_entity;

