DROP DATABASE IF EXISTS springsecurity;
CREATE DATABASE IF NOT EXISTS springsecurity;
USE springsecurity;

DROP TABLE If EXISTS campaigns;
CREATE TABLE campaigns (
	  id          	    int 	    		auto_increment			PRIMARY KEY,
	  active    	    BOOLEAN    			NOT NULL,
	  name              VARCHAR(255)        NOT NULL,
	  type_label        VARCHAR(255)        NOT NULL

);

INSERT INTO campaigns VALUES
(1, 1, 'T-Mobile', 'tech'),
(2, 1, 'Texas Roadhouse', 'food'),
(3, 1, 'Arbys', 'food');

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

INSERT INTO user VALUES
(1, 1, 'pass', 'ROLE_USER', 'user', 0, 0, 0),
(2, 1, 'admin', 'ROLE_ADMIN', 'admin', 0, 0, 0),
(3, 1, 'pass', 'ROLE_BUSINESS', 'business', 0, 0, 0);
