# --- 0.3

# --- !Ups

CREATE TABLE IF NOT EXISTS `cargo_traffic`.`packing_list` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(250) NOT NULL,
  `issue_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `dispatcher` INT(11) NOT NULL,
  `status` VARCHAR(250) NULL DEFAULT NULL,
  `company_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_dispatcher_id` (`dispatcher` ASC),
  INDEX `fk_company_id` (`company_id` ASC),
  CONSTRAINT `fk_dispatcher`
  FOREIGN KEY (`dispatcher`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_company`
  FOREIGN KEY (`company_id`)
  REFERENCES `cargo_traffic`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`vehicle_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`vehicle_fuel` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `cost` DECIMAL(10,2) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`vehicle` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `license_plate` VARCHAR(250) NOT NULL,
  `products_weight` DOUBLE(10,2) NOT NULL,
  `fuel_consumption` DOUBLE(10,2) NOT NULL,
  `company_id` INT(11) NOT NULL,
  `vehicle_type_id` INT(11) NOT NULL,
  `vehicle_fuel_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_company_id` (`company_id` ASC),
  INDEX `fk_vehicle_type_id` (`vehicle_type_id` ASC),
  INDEX `fk_vehicle_fuel_id` (`vehicle_fuel_id` ASC),
  CONSTRAINT `fk_vehicle_company1`
  FOREIGN KEY (`company_id`)
  REFERENCES `cargo_traffic`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_vehicle_type1`
  FOREIGN KEY (`vehicle_type_id`)
  REFERENCES `cargo_traffic`.`vehicle_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_vehicle_fuel1`
  FOREIGN KEY (`vehicle_fuel_id`)
  REFERENCES `cargo_traffic`.`vehicle_fuel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`waybill` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `issue_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `departure_date` TIMESTAMP NULL DEFAULT NULL,
  `status` VARCHAR(250) NOT NULL,
  `departure_warehouse` INT(11) NOT NULL,
  `destination_warehouse` INT(11) NOT NULL,
  `packing_list_id` INT(11) NOT NULL,
  `manager` INT(11) NOT NULL,
  `company_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_departure_warehouse_id` (`departure_warehouse` ASC),
  INDEX `fk_destination_warehouse_id` (`destination_warehouse` ASC),
  INDEX `fk_packing_list_id` (`packing_list_id` ASC),
  INDEX `fk_manager_id` (`manager` ASC),
  INDEX `fk_company_id` (`company_id` ASC),
  CONSTRAINT `fk_packing_list`
  FOREIGN KEY (`packing_list_id`)
  REFERENCES `cargo_traffic`.`packing_list` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_departure_warehouse`
  FOREIGN KEY (`departure_warehouse`)
  REFERENCES `cargo_traffic`.`warehouse` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_destination_warehouse`
  FOREIGN KEY (`destination_warehouse`)
  REFERENCES `cargo_traffic`.`warehouse` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_manager`
  FOREIGN KEY (`manager`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_company_id`
  FOREIGN KEY (`company_id`)
  REFERENCES `cargo_traffic`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE IF NOT EXISTS `cargo_traffic`.`waybill_vehicle_driver` (
  `vehicle_id` INT(11) NOT NULL,
  `driver_id` INT(11) NOT NULL,
  `waybill_id` INT(11) NOT NULL,
  PRIMARY KEY (`vehicle_id`, `driver_id`, `waybill_id`),
  INDEX `fk_user_id` (`driver_id` ASC),
  INDEX `fk_waybill_id` (`waybill_id` ASC),
  CONSTRAINT `fk_vehicle`
  FOREIGN KEY (`vehicle_id`)
  REFERENCES `cargo_traffic`.`vehicle` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver`
  FOREIGN KEY (`driver_id`)
  REFERENCES `cargo_traffic`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_waybill`
  FOREIGN KEY (`waybill_id`)
  REFERENCES `cargo_traffic`.`waybill` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4;

# --- !Downs

DROP TABLE IF EXISTS `cargo_traffic`.`waybill_vehicle_driver`;
DROP TABLE IF EXISTS `cargo_traffic`.`waybill`;
DROP TABLE IF EXISTS `cargo_traffic`.`packing_list`;
DROP TABLE IF EXISTS `cargo_traffic`.`vehicle`;
DROP TABLE IF EXISTS `cargo_traffic`.`vehicle_type`;
DROP TABLE IF EXISTS `cargo_traffic`.`vehicle_fuel`;
