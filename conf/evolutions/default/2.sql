# --- 0.2

# --- !Ups
CREATE TABLE IF NOT EXISTS `cargo_traffic`.`warehouse` (
  `id`      INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `name`    VARCHAR(250)         NOT NULL,
  `address_id` INTEGER(11) UNSIGNED ,

  `deleted`    BIT(1)                        DEFAULT FALSE,
  PRIMARY KEY (`id`),
  INDEX (`address_id` ASC),
  FOREIGN KEY (`address_id`)
  REFERENCES `cargo_traffic`.`address` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `role` (name) VALUES
  ('SYS_ADMIN'), ('ADMIN'), ('DISPATCHER'), ('MANAGER'), ('DRIVER'), ('DIRECTOR');



# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`warehouse`;