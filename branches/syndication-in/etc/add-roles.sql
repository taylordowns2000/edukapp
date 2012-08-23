--
-- Table structure for table `roles`
--
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2606 ;

--
-- Dumping data for table `roles`
-- 

-- ***********************************
-- ***********************************
INSERT INTO `roles` (`id`, `rolename`) VALUES
(2602, 'admin'),
(2603, 'tester'),
(2604, 'moderator'),
(2605, 'guest');

--
-- Table structure for table `useraccount_roles`
--

CREATE TABLE IF NOT EXISTS `useraccount_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `useraccount_roles`
--

INSERT INTO `useraccount_roles` (`user_id`, `role_id`) VALUES
(2001, 2602),
(451, 2602),
(451, 2603),
(451, 2604),
(451, 2605),
(651, 2602),
(651, 2603),
(651, 2604),
(651, 2605);

--
-- Constraints for table `useraccount_roles`
--
ALTER TABLE `useraccount_roles`
  ADD CONSTRAINT `useraccount_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `useraccount` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `useraccount_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

