CREATE TABLE IF NOT EXISTS useraccount (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(20) NOT NULL,
  email varchar(100) NOT NULL,
  password varchar(256) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;