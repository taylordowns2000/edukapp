--<ScriptOptions statementTerminator=";"/>

CREATE TABLE useractivities (
	id INT NOT NULL,
	subject_id INT NOT NULL,
	activity VARCHAR(25) NOT NULL,
	object_id INT NOT NULL,
	time TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP' NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE test (
	id INT NOT NULL,
	name VARCHAR(22) NOT NULL,
	salary FLOAT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE types (
	id INT NOT NULL,
	typetext VARCHAR(64) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE comments (
	id INT NOT NULL,
	commenttext VARCHAR(1024) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE widgetprofiles_tags (
	widgetprofile_id INT NOT NULL,
	tag_id INT NOT NULL,
	PRIMARY KEY (widgetprofile_id,tag_id)
) ENGINE=InnoDB;

CREATE TABLE widgetprofiles (
	id INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	wid_id VARCHAR(150) NOT NULL,
	w3c_or_os BIT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE widgetactivities (
	id INT NOT NULL,
	widgetprofile_id INT NOT NULL,
	activity_id INT NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE activities (
	id INT NOT NULL,
	activitytext VARCHAR(64) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE useraccount (
	id INT NOT NULL,
	username VARCHAR(20) NOT NULL,
	email VARCHAR(100) NOT NULL,
	password VARCHAR(256) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE userreview (
	id INT NOT NULL,
	user_id INT NOT NULL,
	rating TINYINT NOT NULL,
	comment_id INT NOT NULL,
	widgetprofile_id INT NOT NULL,
	time TIMESTAMP DEFAULT 'CURRENT_TIMESTAMP' NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE widgettags (
	widgetprofile_id INT NOT NULL,
	tag_id INT NOT NULL,
	PRIMARY KEY (widgetprofile_id,tag_id)
) ENGINE=InnoDB;

CREATE TABLE tags (
	id INT NOT NULL,
	tagtext VARCHAR(30) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE widget (
	id INT NOT NULL,
	name VARCHAR(255),
	salary DOUBLE,
	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE openjpa_sequence_table (
	ID TINYINT NOT NULL,
	SEQUENCE_VALUE BIGINT,
	PRIMARY KEY (ID)
) ENGINE=InnoDB;

CREATE INDEX tag_id ON widgetprofiles_tags (tag_id ASC);

CREATE INDEX id ON tags (id ASC);

CREATE INDEX widgetprofile_id ON widgetprofiles_tags (widgetprofile_id ASC);

CREATE INDEX id ON widgetprofiles (id ASC);

CREATE INDEX widgetprofile_id ON widgettags (widgetprofile_id ASC);

CREATE INDEX tag_id ON widgettags (tag_id ASC);

