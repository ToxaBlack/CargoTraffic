# --- 0.2

# --- !Ups
CREATE TABLE IF NOT EXISTS `cargo_traffic`.`warehouse` (
  `id`      INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `name`    VARCHAR(250)         NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`warehouse`;