# --- 0.4

# --- !Ups

CREATE TABLE `cargo_traffic`.`measure_unit` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE `cargo_traffic`.`product` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `measure_unit_id` INT(11) UNSIGNED NULL,
  `storage_type` ENUM ('REFRIGERATOR', 'TANK', 'BOXCAR'),
  `deleted` BIT(1) NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `measure_unit_id_idx` (`measure_unit_id` ASC),
  FOREIGN KEY (`measure_unit_id`)
  REFERENCES `cargo_traffic`.`measure_unit` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE `cargo_traffic`.`product_in_packing_list` (
  `product_id` INT(11) UNSIGNED NOT NULL ,
  `packing_list_id` INT(11) UNSIGNED NOT NULL ,
  `price` DECIMAL(10,2) NULL ,
  `count` INT(11) NULL ,
  `status` ENUM('ACCEPTED','VERIFICATION_COMPLETED','DELIVERED','LOST'),
  `deleted` BIT(1) NULL DEFAULT FALSE ,
  PRIMARY KEY (`product_id`, `packing_list_id`) ,
  INDEX `packing_list_idx` (`packing_list_id` ASC) ,
  INDEX `product_idx` (`product_id` ASC) ,
  FOREIGN KEY (`packing_list_id`)
  REFERENCES `cargo_traffic`.`packing_list` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT,
  FOREIGN KEY (`product_id`)
  REFERENCES `cargo_traffic`.`product` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

INSERT INTO `measure_unit` (name) VALUES
  ('KILOGRAM'), ('LITER'), ('SQUARE_METER'), ('PIECES');

# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`product_in_packing_list`;
DROP TABLE IF EXISTS `cargo_traffic`.`product`;
DROP TABLE IF EXISTS `cargo_traffic`.`measure_unit`;

