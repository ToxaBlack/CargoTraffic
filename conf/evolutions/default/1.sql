# --- 0.1

# --- !Ups

CREATE TABLE IF NOT EXISTS `cargo_traffic`.`company` (
  `id`      INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `name`    VARCHAR(250)         NOT NULL,
  `transportation_cost_per_km` DOUBLE(10,2) UNSIGNED NOT NULL DEFAULT '0.00',
  `date`    DATETIME,
  `locked` BIT(1)                        DEFAULT FALSE,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`address` (
  `id`      INTEGER(11)  UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `country` VARCHAR(250),
  `city`    VARCHAR(250),
  `street`  VARCHAR(250),
  `house`   VARCHAR(250),
  `flat`   VARCHAR(250),
  `deleted` BIT(1)                         DEFAULT FALSE,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `cargo_traffic`.`user` (
  `id`         INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `username`   VARCHAR(250)         NOT NULL,
  `password`   VARCHAR(250)         NOT NULL,
  `name`       VARCHAR(250)         ,
  `surname`    VARCHAR(250)         NOT NULL,
  `patronymic` VARCHAR(250)         ,
  `email`      VARCHAR(250)         NOT NULL,
  `birthday`   DATE                 ,
  `company_id` INTEGER(11) UNSIGNED,
  `address_id` INTEGER(11) UNSIGNED ,
  `deleted`    BIT(1)                        DEFAULT FALSE,
  PRIMARY KEY (`id`),
  INDEX (`company_id` ASC),
  INDEX (`address_id` ASC),
  FOREIGN KEY (`company_id`)
  REFERENCES `cargo_traffic`.`company` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (`address_id`)
  REFERENCES `cargo_traffic`.`address` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`role` (
  `id`   INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `name` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`user_role` (
  `user_id` INTEGER(11) UNSIGNED NOT NULL,
  `role_id` INTEGER(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX (`role_id`),
  FOREIGN KEY (`user_id`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  FOREIGN KEY (`role_id`)
  REFERENCES `cargo_traffic`.`role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;




# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`user_role`;

DROP TABLE IF EXISTS `cargo_traffic`.`user`;

DROP TABLE IF EXISTS `cargo_traffic`.`role`;

DROP TABLE IF EXISTS `cargo_traffic`.`address`;

DROP TABLE IF EXISTS `cargo_traffic`.`company`;
