-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 29, 2012 at 01:05 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `edukapptests`
--

--
-- Dumping data for table `accountinfo`
--

INSERT INTO `accountinfo` (`id`, `realname`, `website`, `joined`, `lastseen`, `shortbio`) VALUES
(51, 'edukapp', NULL, '2012-05-29 05:00:48', NULL, NULL);

--
-- Dumping data for table `activities`
--

INSERT INTO `activities` (`id`, `activitytext`) VALUES
(2652, 'collaboration'),
(2653, 'gaming'),
(2654, 'searching'),
(2655, 'researching'),
(2656, 'navigation');

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`id`, `commenttext`) VALUES
(201, 'This is a test review');

--
-- Dumping data for table `ltiprovider`
--

INSERT INTO `ltiprovider` (`id`, `consumerKey`, `consumerSecret`) VALUES
(51, 'TEST', 'TEST');

--
-- Dumping data for table `openjpa_sequence_table`
--

INSERT INTO `openjpa_sequence_table` (`ID`, `SEQUENCE_VALUE`) VALUES
(0, 451);

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`id`, `tagtext`) VALUES
(251, 'voting'),
(401, 'collab'),
(402, 'planning'),
(403, 'organization'),
(404, 'chat');

--
-- Dumping data for table `useraccount`
--

INSERT INTO `useraccount` (`id`, `username`, `email`, `password`, `salt`, `token`) VALUES
(51, 'edukapp', 'edukapp@edukapp.com', '4d38530fdea5dfc40e76f25a360b0cde', '2581336a-088d-476b-b45d-93e9efc3c9fc', '02');

--
-- Dumping data for table `useractivities`
--

INSERT INTO `useractivities` (`id`, `subject_id`, `activity`, `object_id`, `time`) VALUES
(101, 51, 'joined', 0, '2012-05-29 05:00:48'),
(102, 51, 'commented', 2552, '2012-05-29 05:01:19'),
(103, 51, 'tagged', 2552, '2012-05-29 05:01:41'),
(104, 51, 'updatedescription', 2552, '2012-05-29 05:06:22'),
(301, 51, 'updatedescription', 2553, '2012-05-29 10:45:13'),
(302, 51, 'updatedescription', 2554, '2012-05-29 10:45:33'),
(303, 51, 'updatedescription', 2555, '2012-05-29 10:54:33'),
(304, 51, 'updatedescription', 2557, '2012-05-29 10:57:45'),
(305, 51, 'updatedescription', 2558, '2012-05-29 10:58:12'),
(306, 51, 'updatedescription', 2559, '2012-05-29 10:58:41'),
(307, 51, 'updatedescription', 2560, '2012-05-29 10:59:02'),
(308, 51, 'updatedescription', 2561, '2012-05-29 10:59:25'),
(309, 51, 'updatedescription', 2562, '2012-05-29 10:59:41'),
(310, 51, 'updatedescription', 2563, '2012-05-29 11:00:12'),
(311, 51, 'updatedescription', 2564, '2012-05-29 11:01:05'),
(312, 51, 'updatedescription', 2565, '2012-05-29 11:01:29'),
(313, 51, 'bindactivity', 2552, '2012-05-29 11:02:52'),
(314, 51, 'tagged', 2552, '2012-05-29 11:03:06'),
(315, 51, 'tagged', 2553, '2012-05-29 11:03:22'),
(316, 51, 'tagged', 2553, '2012-05-29 11:03:33'),
(317, 51, 'bindactivity', 2553, '2012-05-29 11:03:38'),
(318, 51, 'tagged', 2554, '2012-05-29 11:03:52'),
(319, 51, 'tagged', 2554, '2012-05-29 11:03:57'),
(320, 51, 'bindactivity', 2554, '2012-05-29 11:03:59');

--
-- Dumping data for table `userreview`
--

INSERT INTO `userreview` (`id`, `user_id`, `comment_id`, `widgetprofile_id`, `time`) VALUES
(151, 51, 201, 2552, '2012-05-29 05:01:19');

--
-- Dumping data for table `widgetactivities`
--

INSERT INTO `widgetactivities` (`id`, `widgetprofile_id`, `activity_id`) VALUES
(77, 2552, 2652),
(78, 2553, 2652),
(79, 2554, 2652);

--
-- Dumping data for table `widgetprofiles`
--

INSERT INTO `widgetprofiles` (`id`, `name`, `wid_id`, `w3c_or_os`, `featured`, `DESCRIPTION_WID_ID`, `icon`, `created`, `updated`) VALUES
(5, 'SimpleChat', 'http://wookie.apache.org/widgets/simplechat', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/wookie.apache.org/widgets/simplechat/icon.png', '2012-05-29', '2012-05-29'),
(6, 'videoplayer', 'http://widgets.open.ac.uk/kmi/stellar/videoplayerembedcode', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/widgets.open.ac.uk/kmi/stellar/videoplayerembedcode/images/stellarnet-widget-icon.png', '2012-05-29', '2012-05-29'),
(7, 'ROLE ObjectSpot Gadget', 'http://www.getwookie.org/widgets/role-objectspot', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/www.getwookie.org/widgets/role-objectspot/icon.png', '2012-05-29', '2012-05-29'),
(8, 'sharedEmbedWidget', 'http://widgets.open.ac.uk/kmi/stellar/sharedembedwidget', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/widgets.open.ac.uk/kmi/stellar/sharedembedwidget/images/stellarnet-widget-icon.png', '2012-05-29', '2012-05-29'),
(351, 'graaasp', 'http://graaasp.epfl.ch/widgets/graaasp', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/graaasp.epfl.ch/widgets/graaasp/images/graaasp.png', '2012-05-29', '2012-05-29'),
(352, 'XING Gadget', 'http://igoogle.xing.com/gadget.xml', 0, 0, NULL, 'http://igoogle.xing.com/images/thumbnail.png', '2012-05-29', '2012-05-29'),
(353, 'ROLE FlashMeeting Gadget', 'http://fm.ea-tel.eu/gadgets/fm_gadget.xml', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/shared/images/defaultwidget.png', '2012-05-29', '2012-05-29'),
(354, 'TwitterGadget', 'http://www.twittergadget.com/gadget_gmail.xml', 0, 0, NULL, 'http://www.twittergadget.com/images/thumbnail2.png', '2012-05-29', '2012-05-29'),
(355, 'Super Flash Mario Bros', 'http://games.uflaz.com/xml/superflashmariobros.xml', 0, 0, NULL, 'http://games.uflaz.com//images/superflashmariobros.jpg', '2012-05-29', '2012-05-29'),
(356, 'Visualizer', 'http://widgets.open.ac.uk/stellarnet/visualizer', 0, 0, NULL, NULL, '2012-05-29', '2012-05-29'),
(357, 'minietherpad', 'http://widgets.open.ac.uk/kmi/stellar/minietherpad', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/widgets.open.ac.uk/kmi/stellar/minietherpad/images/stellarnet-widget-icon.png', '2012-05-29', '2012-05-29'),
(358, 'Conference Visualization', 'http://kmi.open.ac.uk/conferencevis', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/kmi.open.ac.uk/conferencevis/images/icon.png', '2012-05-29', '2012-05-29'),
(359, 'A Simile Timeline widget', 'http://www.joanneum.at/simileWidget', 0, 0, NULL, 'http://widgets.open.ac.uk:8080/wookie/wservices/www.joanneum.at/simileWidget/icon.png', '2012-05-29', '2012-05-29'),
(360, 'Twitter Streamgraph Visualisation', 'http://incubator.apache.org/wookie/generated/FEA04335-C41F-7FB9-9FB7-CE49BD88E297', 0, 0, NULL, NULL, '2012-05-29', '2012-05-29'),
(2552, 'You decide', 'http://www.getwookie.org/widgets/youdecide', 0, 1, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/youdecide/icon.png', '2012-05-29', '2012-05-29'),
(2553, 'Ta-Da!', 'http://www.getwookie.org/widgets/todo', 0, 1, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/camera/icon.svg', '2012-05-29', '2012-05-29'),
(2554, 'Natter', 'http://www.getwookie.org/widgets/natter', 0, 1, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/natter/icon.png', '2012-05-29', '2012-05-29'),
(2555, 'Bubbles', 'http://www.opera.com/widgets/bubbles', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/www.opera.com/widgets/bubbles/icon_64.png', '2012-05-29', '2012-05-29'),
(2556, 'SimpleChat', 'http://wookie.apache.org/widgets/simplechat', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/simplechat/icon.png', '2012-05-29', '2012-05-29'),
(2557, 'freeder', 'http://wookie.apache.org/widgets/freeder', 0, 1, NULL, 'http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/freeder/images/icon.png', '2012-05-29', '2012-05-29'),
(2558, 'WookieWiki', 'http://www.getwookie.org/widgets/wiki', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/wiki/icon.png ', '2012-05-29', '2012-05-29'),
(2559, 'geo', 'http://wookie.apache.org/widgets/geo', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/geo/icon.svg', '2012-05-29', '2012-05-29'),
(2560, 'Wave Test: Sudoku', 'http://www.getwookie.org/widgets/sudoku', 0, 1, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/sudoku/icon.png', '2012-05-29', '2012-05-29'),
(2561, 'Butterfly', 'http://wookie.apache.org/widgets/butterfly', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/butterfly/images/icon.png', '2012-05-29', '2012-05-29'),
(2562, 'Weather', 'http://www.getwookie.org/widgets/weather', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/weather/icon.png', '2012-05-29', '2012-05-29'),
(2563, 'ShareDraw', 'http://wookie.apache.org/widgets/sharedraw', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/sharedraw/images/icon.png', '2012-05-29', '2012-05-29'),
(2564, 'Camera', 'http://www.getwookie.org/widgets/camera', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/www.getwookie.org/widgets/camera/icon.svg', '2012-05-29', '2012-05-29'),
(2565, 'Write That Thesis!', 'http://role-project.eu/widgets/writethatthesis', 0, 0, NULL, 'http://localhost:8080/wookie/wservices/role-project.eu/widgets/writethatthesis/icon.png', '2012-05-29', '2012-05-29');

--
-- Dumping data for table `widgetprofiles_tags`
--

INSERT INTO `widgetprofiles_tags` (`widgetprofile_id`, `tag_id`) VALUES
(2552, 251),
(2552, 401),
(2553, 402),
(2553, 403),
(2554, 401),
(2554, 404);

--
-- Dumping data for table `widgetstats`
--

INSERT INTO `widgetstats` (`wid_id`, `downloads`, `embeds`, `views`, `averageRating`, `totalRatings`) VALUES
(1, 0, 0, 0, NULL, NULL),
(2, 0, 0, 0, NULL, NULL),
(3, 0, 0, 0, NULL, NULL),
(4, 0, 0, 0, NULL, NULL),
(5, 0, 0, 0, NULL, NULL),
(6, 0, 0, 0, NULL, NULL),
(7, 0, 0, 0, NULL, NULL),
(8, 0, 0, 0, NULL, NULL),
(351, 0, 0, 0, NULL, NULL),
(352, 0, 0, 0, NULL, NULL),
(353, 0, 0, 0, NULL, NULL),
(354, 0, 0, 0, NULL, NULL),
(355, 0, 0, 0, NULL, NULL),
(356, 0, 0, 0, NULL, NULL),
(357, 0, 0, 0, NULL, NULL),
(358, 0, 0, 0, NULL, NULL),
(359, 0, 0, 0, NULL, NULL),
(360, 0, 0, 0, NULL, NULL),
(2552, 1, 1, 1, 2, 2),
(2553, 0, 1, 2, 4, 3),
(2554, 2, 4, 5, 3, 3),
(2555, 1, 1, 1, 1, 1),
(2556, 0, 0, 0, 0, 0),
(2557, 0, 0, 0, 0, 0),
(2558, 0, 0, 0, 0, 0),
(2559, 0, 0, 0, 0, 0),
(2560, 0, 0, 0, 0, 0),
(2561, 0, 0, 0, 0, 0),
(2562, 0, 0, 0, 0, 0),
(2563, 0, 0, 0, 0, 0),
(2564, 0, 0, 0, 0, 0),
(2565, 0, 0, 0, 0, 0);

--
-- Dumping data for table `widget_descriptions`
--

INSERT INTO `widget_descriptions` (`wid_id`, `description`) VALUES
(2552, 'A quick and simple voting widget'),
(5, 'Stripped down chat widget with minimal styling'),
(6, 'A videoplayer, which lets you display videos using their embed code'),
(7, 'The ObjectSpot search gadget allows you to find online resources from a variety of repositories, including OpenLearn, iTunesU, Slideshare, YouTube, and Wikipedia.'),
(8, 'A widget, which lets you embed html snippets, for example to display videos or webpages'),
(2553, 'A shared to-do list widget'),
(2554, 'basic chat widget'),
(351, 'Visualizes the list of spaces for the Graaasp logged in user. In case the user is not logged-in, the spaces for guest account are shown. The widget also supports (soon) drag and drop to add papers into given spaces.'),
(2555, 'A Bubbles game'),
(2556, 'SimpleChat'),
(2557, 'An RSS reader widget optimised for small screens or desktop widgets.'),
(2558, 'A Wiki In A Widget'),
(2559, 'An example of a HTML 5 geolocation widget.'),
(352, 'Google Gadget'),
(353, 'Google Gadget'),
(354, 'Google Gadget'),
(355, 'Google Gadget'),
(2560, 'Google Wave Sudoku Widget'),
(2561, 'Paint colourful butterflies!'),
(2562, 'A silly Weather widget'),
(356, 'visualize friends on teleurope community'),
(357, 'Collaborative writing with an etherpad'),
(358, 'Visualizes the collaboration networks of researchers'),
(2563, 'A collaborative drawing application - sketch with friends!'),
(359, 'Simile widget for demonstration purposes'),
(360, 'Using Grafico javscript charting library'),
(2564, 'A widget demonstrating the use of the BONDI camera API'),
(2565, 'Yes, we know you''ve got a lot to do but that thesis won''t write itself so get a move on! Use this widget to track how you''re progressing to that first draft');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
