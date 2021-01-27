-----------------------------------------------------------------------------------------------------------------------------------------
-- setting up a new schema:
-----------------------------------------------------------------------------------------------------------------------------------------

DROP SCHEMA IF EXISTS "meeting_user_data" CASCADE;
CREATE SCHEMA "meeting_user_data" AUTHORIZATION cwhjeextvmzolm;


-----------------------------------------------------------------------------------------------------------------------------------------
-- creating new tables for the schema:
-----------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "meeting_user_data"."meeting";
CREATE TABLE "meeting_user_data"."meeting" (
  "id" serial,
  "name" character varying(100) NOT NULL UNIQUE,
  "date_time" timestamp without time zone NOT NULL,
  "display" boolean DEFAULT true,
  "created" timestamp without time zone DEFAULT now(),
  "last_updated" timestamp without time zone DEFAULT now(),
  PRIMARY KEY (id)
);

-----------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "meeting_user_data"."user";
CREATE TABLE "meeting_user_data"."user" (
  "id" serial,
  "username" character varying(20) NOT NULL UNIQUE,
  "password" character(60) NOT NULL,
  "firstname" character varying(50) NOT NULL,
  "lastname" character varying(50) NOT NULL,
  "email" character varying(100) NOT NULL UNIQUE,
  "company" character varying(100) NOT NULL,
  "created" timestamp without time zone DEFAULT now(),
  "last_updated" timestamp without time zone DEFAULT now(),
  "reset_password_secret" character(255),
  PRIMARY KEY (id)
);

-----------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "meeting_user_data"."role";
CREATE TABLE "meeting_user_data"."role" (
  "id" serial,
  "name" character varying(20) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-----------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "meeting_user_data"."user_role";
CREATE TABLE "meeting_user_data"."user_role" (
  "id" serial,
  "id_user" integer NOT NULL UNIQUE,
  "id_role" integer NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_user) REFERENCES "meeting_user_data"."user"(id) ON DELETE CASCADE,
  FOREIGN KEY (id_role) REFERENCES "meeting_user_data"."role"(id) ON DELETE CASCADE
);

-----------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "meeting_user_data"."meeting_user";
CREATE TABLE "meeting_user_data"."meeting_user" (
  "id" serial,
  "id_meeting" integer NOT NULL,
  "id_user" integer NOT NULL,
  "created" timestamp without time zone DEFAULT now(),
  "last_updated" timestamp without time zone DEFAULT now(),
  PRIMARY KEY (id),
  FOREIGN KEY (id_meeting) REFERENCES "meeting_user_data"."meeting"(id) ON DELETE CASCADE,
  FOREIGN KEY (id_user) REFERENCES "meeting_user_data"."user"(id) ON DELETE CASCADE
);


-----------------------------------------------------------------------------------------------------------------------------------------
-- filling up the new tables with sample data:
-----------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "meeting_user_data"."meeting" (
  "name",
  "date_time") 
VALUES
  ('Thementag - Wie leite ich ein Thema richtig ein?', '2020-10-01 10:00:00'),
  ('Ernährungsberatung', '2020-10-01 12:00:00'),
  ('Workshop Datenverwaltung', '2020-10-01 14:00:00'),
  ('Vortrag Algorithmen', '2020-10-20 10:30:00'),
  ('Vortrag Datenstrukturen', '2020-10-20 12:30:00'),
  ('SQL Einführung', '2020-10-06 08:30:00'),
  ('Einführung in komplexe Systeme', '2020-10-06 15:00:00');

-----------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "meeting_user_data"."user" (
  "username",
  "password",
  "firstname",
  "lastname",
  "email",
  "company") 
VALUES
  ('admin', '$2a$10$5L6RdmKXIm4QBNgNIV0kU.lNglfZ6IcWjy2efHS8t3OYK9ohQ2LZK', 'admin', 'admin', 'admin', 'admin'),
  ('jdun', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Jax', 'Dunlop', 'J.Dunlop@dunlop-gmbh.com', 'Dunlop'),
  ('mmus', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Max', 'Mustermann', 'M.Mustermann@beispielfirma.com', 'Beispielfirma'),
  ('gdin', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Gerda', 'Dinkel', 'G.Dinkel@email.com', 'Post'),
  ('sfle', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Sammy', 'Fleischbällchen', 'S.Fleisch@hotmail.com', 'Burgerking'),
  ('smue', '$2a$10$tdn0T9dQWeXSJ6NO/PGCe.2rrHfpd1BihEVADHVGqbzQffhF0bF6u', 'Sabine', 'Müller', 'S.Müller@gmail.com', 'DHL');
    
-----------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "meeting_user_data"."role" (
  "id",
  "name")
VALUES
  (1, 'ROLE_ADMIN'),
  (2, 'ROLE_USER');
    
-----------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "meeting_user_data"."user_role" (
  "id_user",
  "id_role")
VALUES
  (1, 1),
  (2, 2),
  (3, 2),
  (4, 2),
  (5, 2),
  (6, 2);
    
-----------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO "meeting_user_data"."meeting_user" (
  "id_meeting",
  "id_user") 
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
