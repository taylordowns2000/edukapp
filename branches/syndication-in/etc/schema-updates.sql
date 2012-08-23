-- ALTER TABLE `useractivities` CHANGE `time` `time` DATE NOT NULL;
-- nope that's a mistake revert it back to timestamp

--
-- Constraints for table `accountinfo`
--
ALTER TABLE `accountinfo`
  ADD CONSTRAINT `accountinfo_ibfk_1` FOREIGN KEY (`id`) REFERENCES `useraccount` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `useraccount_roles`
--
ALTER TABLE `useraccount_roles`
  ADD CONSTRAINT `useraccount_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `useraccount` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `useraccount_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `userratings`
--
ALTER TABLE `userratings`
  ADD CONSTRAINT `userratings_ibfk_2` FOREIGN KEY (`widgetprofile_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `userratings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `useraccount` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `widgetactivities`
--
ALTER TABLE `widgetactivities`
  ADD CONSTRAINT `widgetactivities_ibfk_2` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `widgetactivities_ibfk_1` FOREIGN KEY (`widgetprofile_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `widgetprofiles_tags`
--
ALTER TABLE `widgetprofiles_tags`
  ADD CONSTRAINT `widgetprofiles_tags_ibfk_2` FOREIGN KEY (`widgetprofile_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `widgetprofiles_tags_ibfk_1` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `widget_descriptions`
--
ALTER TABLE `widget_descriptions`
  ADD CONSTRAINT `widget_descriptions_ibfk_1` FOREIGN KEY (`wid_id`) REFERENCES `widgetprofiles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
