# --- 0.5

# --- !Ups
CREATE TABLE  IF NOT EXISTS `cargo_traffic`.`waypoint` (
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

CREATE TABLE  IF NOT EXISTS `cargo_traffic`.`product_in_wvd` (
  `waybill_id` INT(11) UNSIGNED NOT NULL,
  `vehicle_id` INT(11) UNSIGNED NOT NULL,
  `driver_id` INT(11) UNSIGNED NOT NULL,
  `product_id` INT(11) UNSIGNED NOT NULL,
  `quantity` INT(11) UNSIGNED NULL,
  PRIMARY KEY (`waybill_id`, `vehicle_id`, `driver_id`, `product_id`),
  INDEX `product_in_wvd_vehicle_fk_idx` (`vehicle_id` ASC),
  INDEX `product_in_wvd_driver_idx` (`driver_id` ASC),
  INDEX `product_in_wvd_product_fk_idx` (`product_id` ASC),
  CONSTRAINT `product_in_wvd_waybill_fk`
  FOREIGN KEY (`waybill_id`)
  REFERENCES `cargo_traffic`.`waybill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_in_wvd_vehicle_fk`
  FOREIGN KEY (`vehicle_id`)
  REFERENCES `cargo_traffic`.`vehicle` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_in_wvd_driver_fk`
  FOREIGN KEY (`driver_id`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_in_wvd_product_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `cargo_traffic`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_in_wvd_waybill_wvd_fk`
  FOREIGN KEY (`waybill_id` , `vehicle_id` , `driver_id`)
  REFERENCES `cargo_traffic`.`waybill_vehicle_driver` (`waybill_id` , `vehicle_id` , `driver_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`waypoint`;
DROP TABLE IF EXISTS `cargo_traffic`.`product_in_wvd`;
