DROP TABLE If EXISTS campaigns;
CREATE TABLE campaigns (
	  id          	    int 	    		auto_increment			PRIMARY KEY,
	  active    	    BOOLEAN    			NOT NULL,
	  name              VARCHAR(255)        NOT NULL,
	  type_label        VARCHAR(255)        NOT NULL,
	  number_assigned   int                 DEFAULT 0,
	  number_completed  int                 DEFAULT 0

);

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

