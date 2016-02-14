# --- 0.5

# --- !Ups
CREATE TABLE `cargo_traffic`.`waypoint` (
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

CREATE TABLE `cargo_traffic`.`product_in_wvd` (
  `wvd_id` INT(11) UNSIGNED NOT NULL,
  `product_id` INT(11) UNSIGNED NOT NULL,
  `quantity` INT(11) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`wvd_id`, `product_id`),
  INDEX `product_in_wvd_product_fk_idx` (`product_id` ASC),
  CONSTRAINT `product_in_wvd_product_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `cargo_traffic`.`product` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `product_in_wvd_wvd_fk`
  FOREIGN KEY (`wvd_id`)
  REFERENCES `cargo_traffic`.`waybill_vehicle_driver` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`waypoint`;
DROP TABLE IF EXISTS `cargo_traffic`.`product_in_wvd`;
