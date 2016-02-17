# --- Testing data

# --- !Ups


INSERT INTO `cargo_traffic`.`company` (name, date) VALUES
  ("TradeCorp", "2015-01-01 10-00-00"),
  ("TravelInc", "2010-01-01 11-00-00"),
  ("SomeInc", "2005-01-01 12-00-00"),
  ("Test", "2000-01-01 13-00-00");


INSERT INTO `address` (country, city, street, house, flat) VALUES
  ("Belarus", "Minsk", "blabla", "12", "1");

INSERT INTO `cargo_traffic`.`warehouse` (name,address_id) VALUES
  ("Торговая сила",1),
  ("IBM",1),
  ("Торговая сила",1),
  ("Склад 11",1);


INSERT INTO `user` (username, password, name, surname, patronymic, email, birthday, company_id, address_id) VALUES
  ("sys_admin", "$2a$10$nsE/Rt.2CteTvuLVvp64y.PC2y4/lcGGSIODMAvlCsew6stOoWMFi", "poll", "simson", "васильевич", "test@mail.ru", "1994-1-6", NULL, 1),
  ("admin1", "$2a$10$ipSFEtPaAjLraBNNezM5UuZImzmjWdQVbFkGMiTLWjoj3HceSh1cS", "tom", "brown", "васильевич", "test@mail.ru", "1994-1-6", 1, 1),
  ("admin2", "$2a$10$91Y1.6P1D.q67quSRfykuOBvS7NJSHAU.YsXXbLdKeugYcpizvyy.", "bob", "black", "васильевич", "test@mail.ru", "1994-1-6", 2, 1),
  ("admin3", "$2a$10$tNaKU9sTQJXOgqDso7hameAHdruldJEwtXJOmOo89dR7bzFLTuYQG", "robert", "cottrell", "васильевич", "test@mail.ru", "1994-1-6", 3, 1),
  ("dispatcher", "$2a$10$Mgs4qaUzqPIIm8TrkIX0ReeVP/1yENNpywjQ3UwWxWgZfmRFUO0ly", "chris", "richards", "васильевич", "test@mail.ru", "1994-1-6", 2, 1),
  ("manager", "$2a$10$NUbCCT3pBeGnr..Lla7hauWKNpaM.dIalrDUCqeWhKo2DeIZaqUVW", "tom", "brown", "васильевич", "test@mail.ru", "1994-1-6", 2, 1),
  ("driver", "$2a$10$oKnV374KGmKCBOE0CbYiHeSnV3M9HaBpEXK./rForcOh5yYA4wfR.", "chris", "richards", "васильевич", "test@mail.ru", "1994-1-6", 2, 1);


INSERT INTO `user_role` (user_id, role_id) VALUES
  (1, 1),
  (2, 2),
  (3, 2),
  (4, 2),
  (5, 3),
  (6, 4),
  (7, 5);

INSERT INTO `vehicle_type` (`name`) VALUES
  ("Box"),
  ("Refrigerator"),
  ("Tank");

INSERT INTO `vehicle_fuel` (`name`, `cost`) VALUES
  ("Diesel", "25.03"),
  ("Bio-diesel", "22.34"),
  ("Petrol-95", "20.45"),
  ("Petrol-98", "21.23");

INSERT INTO `vehicle` (`vehicle_producer`, `vehicle_model`, `license_plate`, `products_constraint`, `fuel_consumption`, `company_id`, `vehicle_type_id`, `vehicle_fuel_id`, `deleted`) VALUES
  ("Man", "A123", "1234-AB", 1234.3, 15.3, 1, 2, 3, 0),
  ("Man", "B234", "6543-QW", 2345.6, 20.5, 1, 2, 1, 0),
  ("Man", "C345", "4567-RT", 3456.75, 17.8, 2, 3, 4, 0);

# --- !Downs
