# --- 0.6

# --- !Ups
CREATE TABLE IF NOT EXISTS `cargo_traffic`.`lost_product` (
  `product_id` INT(11) UNSIGNED NOT NULL,
  `quantity` INT(11) UNSIGNED NULL DEFAULT 0,
  `driver_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`product_id`, `driver_id`),
  UNIQUE INDEX `lost_product_idx` (`driver_id`, `product_id` ASC),
  CONSTRAINT `lost_product_product_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `cargo_traffic`.`product` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `lost_product_driver_fk`
  FOREIGN KEY (`driver_id`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`lost_product`;