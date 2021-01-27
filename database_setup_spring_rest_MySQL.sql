#########################################################################################################################################
# setting up an new user:
#########################################################################################################################################

DROP USER IF EXISTS 'spring-rest'@'localhost';
CREATE USER 'spring-rest'@'localhost' IDENTIFIED BY 'spring-rest';
GRANT ALL PRIVILEGES ON *.* TO 'spring-rest'@'localhost';
ALTER USER 'spring-rest'@'localhost' IDENTIFIED WITH mysql_native_password BY 'spring-rest';


#########################################################################################################################################
# setting up a new database:
#########################################################################################################################################

DROP DATABASE IF EXISTS `spring-rest`;
CREATE DATABASE `spring-rest`;


#########################################################################################################################################
# creating new tables for the database:
#########################################################################################################################################

DROP TABLE IF EXISTS `spring-rest`.`meeting`;
CREATE TABLE `spring-rest`.`meeting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE,
  `date_time` datetime NOT NULL,
  `display` tinyint(1) DEFAULT '1',
  `created` datetime DEFAULT now(),
  `last_updated` datetime DEFAULT now(),
  PRIMARY KEY (`id`)
);

#########################################################################################################################################

DROP TABLE IF EXISTS `spring-rest`.`user`;
CREATE TABLE `spring-rest`.`user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL UNIQUE,
  `password` char(60) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `company` varchar(100) NOT NULL,
  `created` datetime DEFAULT now(),
  `last_updated` datetime DEFAULT now(),
  `reset_password_secret` char(255),
  PRIMARY KEY (`id`)
);

#########################################################################################################################################

DROP TABLE IF EXISTS `spring-rest`.`role`;
CREATE TABLE `spring-rest`.`role` (
  `id` int NOT NULL,
  `name` varchar(20) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
);

#########################################################################################################################################

DROP TABLE IF EXISTS `spring-rest`.`user_role`;
CREATE TABLE `spring-rest`.`user_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL UNIQUE,
  `id_role` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_user`) REFERENCES `spring-rest`.`user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`id_role`) REFERENCES `spring-rest`.`role`(`id`) ON DELETE CASCADE
);

#########################################################################################################################################

DROP TABLE IF EXISTS `spring-rest`.`meeting_user`;
CREATE TABLE `spring-rest`.`meeting_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_meeting` INT NOT NULL,
  `id_user` INT NOT NULL,
  `created` datetime DEFAULT now(),
  `last_updated` datetime DEFAULT now(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_meeting`) REFERENCES `spring-rest`.`meeting`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`id_user`) REFERENCES `spring-rest`.`user`(`id`) ON DELETE CASCADE
);


#########################################################################################################################################
# filling up the new tables with sample data:
#########################################################################################################################################

INSERT INTO `spring-rest`.`meeting` (
    `name`,
    `date_time`) 
VALUES
	('Thementag - Wie leite ich ein Thema richtig ein?', '2020-10-01 10:00:00'),
	('Ernährungsberatung', '2020-10-01 12:00:00'),
	('Workshop Datenverwaltung', '2020-10-01 14:00:00'),
	('Vortrag Algorithmen', '2020-10-20 10:30:00'),
	('Vortrag Datenstrukturen', '2020-10-20 12:30:00'),
	('SQL Einführung', '2020-10-06 08:30:00'),
	('Einführung in komplexe Systeme', '2020-10-06 15:00:00');

#########################################################################################################################################

INSERT INTO `spring-rest`.`user` (
    `username`,
    `password`,
    `firstname`,
    `lastname`,
    `email`,
    `company`) 
VALUES
	('admin', '$2a$10$5L6RdmKXIm4QBNgNIV0kU.lNglfZ6IcWjy2efHS8t3OYK9ohQ2LZK', 'admin', 'admin', 'admin', 'admin'),
	('jdun', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Jax', 'Dunlop', 'J.Dunlop@dunlop-gmbh.com', 'Dunlop'),
	('mmus', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Max', 'Mustermann', 'M.Mustermann@beispielfirma.com', 'Beispielfirma'),
	('gdin', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Gerda', 'Dinkel', 'G.Dinkel@email.com', 'Post'),
	('sfle', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Sammy', 'Fleischbällchen', 'S.Fleisch@hotmail.com', 'Burgerking'),
	('smue', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Sabine', 'Müller', 'S.Müller@gmail.com', 'DHL');
    
#########################################################################################################################################

INSERT INTO `spring-rest`.`role` (
    `id`,
    `name`)
VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_USER');
    
#########################################################################################################################################

INSERT INTO `spring-rest`.`user_role` (
    `id_user`,
    `id_role`)
VALUES
	(1, 1),
	(2, 2),
	(3, 2),
	(4, 2),
	(5, 2),
	(6, 2);
    
#########################################################################################################################################

INSERT INTO `spring-rest`.`meeting_user` (
    `id_meeting`,
    `id_user`) 
VALUES
	(1, 2),
	(2, 2),
	(3, 2),
	(1, 3),
	(3, 3),
	(5, 3),
	(2, 4),
	(3, 4),
	(4, 4),
	(3, 5),
	(4, 5),
	(6, 5),
	(4, 6),
	(6, 6),
	(7, 6);
