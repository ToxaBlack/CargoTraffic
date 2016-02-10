# --- Create database and user !!!Manually!!! Apply by hands!!!!!!!

# --- !Ups

CREATE DATABASE IF NOT EXISTS `cargo_traffic`
  CHARACTER SET 'utf8mb4'
  COLLATE 'utf8mb4_general_ci';

CREATE USER 'freight_admin'@'%' IDENTIFIED BY '7654321';
GRANT ALL PRIVILEGES ON cargo_traffic . * TO 'freight_admin'@'%';

# --- !Downs

DROP USER IF EXISTS 'freight_admin'@'%';

DROP DATABASE IF EXISTS `cargo_traffic`;