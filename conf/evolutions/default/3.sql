# --- Testing data

# --- !Ups


INSERT INTO `cargo_traffic`.`company` (name) VALUES
  ("TradeCorp"),
  ("TravelInc"),
  ("SomeInc");

INSERT INTO `address` (country, city, street, house, flat) VALUES
  ("Belarus", "Minsk", "blabla", "12", "1");



INSERT INTO `user` (username, password, name, surname, patronymic, email, birthday, company_id, address_id) VALUES
  ("admin", "$2a$12$i1UEuEN99CNIsPfrEAcaeuP0pkLlVvGc2jnrRZqKaX/Osylh10WSG", "tom", "brown", "васильевич", "test@mail.ru", "1994-1-6", 1, 1),
  ("user", "$2a$12$HGlcYAg/Z/IoZj2.D3PgsOqopzoHacNvw4jjpAZSRagmBpOVvz1pm", "tom", "brown", "васильевич", "test@mail.ru", "1994-1-6", 2, 1);

#password == username

INSERT INTO `role` (name) VALUES
  ("SYS_ADMIN"), ("USER");

INSERT INTO `user_role` (user_id, role_id) VALUES
  (1, 1),
  (2, 2);

# --- !Downs
