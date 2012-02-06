-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 06, 2012 at 07:12 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `edukapptests`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountinfo`
--

CREATE TABLE IF NOT EXISTS `accountinfo` (
  `id` int(11) NOT NULL,
  `realname` varchar(35) DEFAULT NULL,
  `website` varchar(256) DEFAULT NULL,
  `joined` timestamp NULL DEFAULT NULL,
  `lastseen` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `shortbio` varchar(2056) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `accountinfo`
--

INSERT INTO `accountinfo` (`id`, `realname`, `website`, `joined`, `lastseen`, `shortbio`) VALUES
(951, '2', NULL, '2012-02-06 15:28:16', NULL, NULL),
(1001, 't', NULL, '2012-02-06 16:59:52', '2012-02-06 17:00:10', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `activities`
--

CREATE TABLE IF NOT EXISTS `activities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activitytext` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `activities`
--


-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE IF NOT EXISTS `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commenttext` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `comments`
--


-- --------------------------------------------------------

--
-- Table structure for table `openjpa_sequence_table`
--

CREATE TABLE IF NOT EXISTS `openjpa_sequence_table` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `openjpa_sequence_table`
--

INSERT INTO `openjpa_sequence_table` (`ID`, `SEQUENCE_VALUE`) VALUES
(0, 1051);

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE IF NOT EXISTS `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagtext` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=852 ;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`id`, `tagtext`) VALUES
(1, 'test tag text'),
(2, 'test tag text'),
(651, 'test tag text2'),
(751, 'test tag text2'),
(851, 'test tag text2');

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(22) NOT NULL,
  `salary` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `test`
--


-- --------------------------------------------------------

--
-- Table structure for table `types`
--

CREATE TABLE IF NOT EXISTS `types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typetext` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `types`
--


-- --------------------------------------------------------

--
-- Table structure for table `useraccount`
--

CREATE TABLE IF NOT EXISTS `useraccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(256) NOT NULL,
  `salt` varchar(256) NOT NULL DEFAULT '01',
  `token` varchar(256) NOT NULL DEFAULT '02',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1002 ;

--
-- Dumping data for table `useraccount`
--

INSERT INTO `useraccount` (`id`, `username`, `email`, `password`, `salt`, `token`) VALUES
(951, '2', '2', 'e983ca11c42dbb9e70bf8e7b3d7bb3fd', 'b43fde07-7788-4c1d-9551-51549589e608', '02'),
(1001, 't', 't', '4833b3367b2156e10ee51ee9c546c3b9', '20ba4a0a-de36-4bfb-95ed-80843a2758a2', 'af2a8e7c-ddf6-4e47-ba5e-1a0f4404f6c8');

-- --------------------------------------------------------

--
-- Table structure for table `useractivities`
--

CREATE TABLE IF NOT EXISTS `useractivities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NOT NULL,
  `activity` varchar(25) NOT NULL,
  `object_id` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `useractivities`
--


-- --------------------------------------------------------

--
-- Table structure for table `userreview`
--

CREATE TABLE IF NOT EXISTS `userreview` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `widgetprofile_id` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `userreview`
--


-- --------------------------------------------------------

--
-- Table structure for table `widgetactivities`
--

CREATE TABLE IF NOT EXISTS `widgetactivities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `widgetprofile_id` int(11) NOT NULL,
  `activity_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `widgetactivities`
--


-- --------------------------------------------------------

--
-- Table structure for table `widgetprofiles`
--

CREATE TABLE IF NOT EXISTS `widgetprofiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `wid_id` varchar(150) NOT NULL COMMENT 'either wookie_id or gadget url',
  `w3c_or_os` tinyint(1) NOT NULL,
  `featured` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=902 ;

--
-- Dumping data for table `widgetprofiles`
--

INSERT INTO `widgetprofiles` (`id`, `name`, `wid_id`, `w3c_or_os`, `featured`) VALUES
(1, 'test widget name', 'http://widget-url', 0, 0),
(2, 'test widget name', 'http://widget-url', 0, 0),
(701, 'test widget name2', 'http://widget-url', 0, 0),
(801, 'test widget name2', 'http://widget-url', 0, 0),
(901, 'test widget name2', 'http://widget-url', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `widgetprofiles_tags`
--

CREATE TABLE IF NOT EXISTS `widgetprofiles_tags` (
  `widgetprofile_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`widgetprofile_id`,`tag_id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widgetprofiles_tags`
--


-- --------------------------------------------------------

--
-- Table structure for table `widgettags`
--

CREATE TABLE IF NOT EXISTS `widgettags` (
  `widgetprofile_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`widgetprofile_id`,`tag_id`),
  KEY `widgetprofile_id` (`widgetprofile_id`),
  KEY `tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `widgettags`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `widgettags`
--
ALTER TABLE `widgettags`
  ADD CONSTRAINT `widgettags_ibfk_3` FOREIGN KEY (`widgetprofile_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `widgettags_ibfk_4` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
