DROP DATABASE IF EXISTS springsecurity;
CREATE DATABASE IF NOT EXISTS springsecurity;
USE springsecurity;

DROP TABLE If EXISTS campaigns;
CREATE TABLE campaigns (
	  id          	    int 	    		auto_increment			PRIMARY KEY,
	  active    	    BOOLEAN    			NOT NULL,
	  name              VARCHAR(255)        NOT NULL,
	  type_label        VARCHAR(255)        NOT NULL,
	  number_assigned   int                 DEFAULT 0,
	  number_completed  int                 DEFAULT 0

);

INSERT INTO campaigns (id, active, name, type_label, number_assigned, number_completed) VALUES
(1, 1, 'T-Mobile', 'tech', 0, 0),
(2, 1, 'Texas Roadhouse', 'food', 0, 0),
(3, 1, 'Arbys', 'food', 0, 0);

DROP TABLE If EXISTS user;
CREATE TABLE user (
	  id          	    int 	    		auto_increment			PRIMARY KEY,
	  active    	    BOOLEAN    			DEFAULT 1,
	  password          VARCHAR(255)        NOT NULL,
	  roles             VARCHAR(255)        NOT NULL                CHECK (roles IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_BUSINESS')),
	  user_name         VARCHAR(255)        NOT NULL,
	  advert_one        int                 DEFAULT 0,
	  advert_two        int                 DEFAULT 0,
	  advert_three      int                 DEFAULT 0

);

INSERT INTO user (id, active, password, roles, user_name, advert_one, advert_two, advert_three) VALUES
(1, 1, 'pass', 'ROLE_USER', 'user', 0, 0, 0),
(2, 1, 'admin', 'ROLE_ADMIN', 'admin', 0, 0, 0),
(3, 1, 'pass', 'ROLE_BUSINESS', 'business', 0, 0, 0);
