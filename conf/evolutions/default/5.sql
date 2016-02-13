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


# --- !Downs
DROP TABLE IF EXISTS `cargo_traffic`.`waypoint`;
