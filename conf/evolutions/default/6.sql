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


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`financial_highlights` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `delivered_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `transportation_income` DECIMAL(50,4) UNSIGNED NOT NULL,
  `vehicle_fuel_loss` DECIMAL(50,4) UNSIGNED NOT NULL,
  `products_loss` DECIMAL(50,4) UNSIGNED DEFAULT '0.00',
  `profit` DECIMAL(50,4) DEFAULT '0.00',
  `waybill_vehicle_driver_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`waybill_vehicle_driver_id` ASC),
  FOREIGN KEY (`waybill_vehicle_driver_id`)
  REFERENCES `cargo_traffic`.`waybill_vehicle_driver` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`financial_highlights`;
DROP TABLE IF EXISTS `cargo_traffic`.`lost_product`;