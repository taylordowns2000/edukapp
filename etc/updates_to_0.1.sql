USE edukapptests;

-- 
-- Table structure for table `category`
--
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `grouping` int(11) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=3246;


--
-- Table structure table `favourite`
--
CREATE TABLE IF NOT EXISTS `favourite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `widgetprofile_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `relevance` int(11) NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=4074 ;

-- 
-- Note join tables for the above two tables should be
-- created automatically by jpa
-- 



--
-- Table structure for table `functionality`
--

CREATE TABLE IF NOT EXISTS `functionality` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `uri` varchar(255) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=94 ;


--
-- Table structure for table `widget_functionality`
--

CREATE TABLE IF NOT EXISTS `widget_functionality` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `widgetprofile_id` int(11) NOT NULL,
  `functionality_id` int(11) NOT NULL,
  `relevance` int(11) NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `functionality_id` (`functionality_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


ALTER TABLE `widgetprofiles` ADD publish_level INT(11) NOT NULL DEFAULT '1';
ALTER TABLE `widgetprofiles` ADD deleted tinyint(1) NOT NULL DEFAULT '0';
ALTER TABLE `widgetprofiles` ADD meta_data TEXT;
ALTER TABLE `widgetprofiles` ADD builder VARCHAR(256);
ALTER TABLE `widgetprofiles` ADD owner INT(11);
ALTER TABLE `tags` ADD owner INT(11);


  --
-- Constraints for table `widget_functionality`
--
ALTER TABLE `widget_functionality`
  ADD CONSTRAINT `widget_functionality_ibfk_1` FOREIGN KEY (`widgetprofile_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `widget_functionality_ibfk_2` FOREIGN KEY (`functionality_id`) REFERENCES `functionality` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

