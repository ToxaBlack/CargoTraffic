# --- 0.5

# --- !Ups
CREATE TABLE IF NOT EXISTS `cargo_traffic`.`waypoint` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `lat` FLOAT NOT NULL ,
  `lng` FLOAT NOT NULL ,
  `status` ENUM('CREATED', 'CHECKED', 'UNCHECKED'),
  `waybill_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`) ,
  INDEX `waybill_idx` (`waybill_id` ASC),
  FOREIGN KEY (`waybill_id`)
  REFERENCES `cargo_traffic`.`waybill` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE IF NOT EXISTS `cargo_traffic`.`product_in_wvd` (
  `product_id` INT(11) UNSIGNED NOT NULL,
  `waybill_id` INT(11) UNSIGNED NOT NULL,
  `vehicle_id` INT(11) UNSIGNED NOT NULL,
  `driver_id` INT(11) UNSIGNED NOT NULL,
  `quantity` INT(11) UNSIGNED NULL DEFAULT 0,
  `wvd_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`product_id`, `wvd_id`),
  INDEX `prod_in_wvd_waybill_fk_idx` (`waybill_id` ASC),
  INDEX `prod_in_wvd_vehicle_fk_idx` (`vehicle_id` ASC),
  INDEX `prod_in_wvd_driver_fk_idx` (`driver_id` ASC),
  INDEX `prod_in_wvd_wvd_fk_idx` (`wvd_id` ASC),
  CONSTRAINT `prod_in_wvd_product_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `cargo_traffic`.`product` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `prod_in_wvd_waybill_fk`
  FOREIGN KEY (`waybill_id`)
  REFERENCES `cargo_traffic`.`waybill` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `prod_in_wvd_vehicle_fk`
  FOREIGN KEY (`vehicle_id`)
  REFERENCES `cargo_traffic`.`vehicle` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `prod_in_wvd_driver_fk`
  FOREIGN KEY (`driver_id`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `prod_in_wvd_wvd_fk`
  FOREIGN KEY (`wvd_id`)
  REFERENCES `cargo_traffic`.`waybill_vehicle_driver` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`waypoint`;
DROP TABLE IF EXISTS `cargo_traffic`.`product_in_wvd`;
