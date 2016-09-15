ALTER TABLE `accounts`.`users` 
ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY `users_ibfk_1` (`domainname`) REFERENCES `domains` (`domainname`);

ALTER TABLE `accounts`.`userrole` 
ADD CONSTRAINT `userrole_ibfk_2` FOREIGN KEY `userrole_ibfk_2` (`rolename`) REFERENCES `roles` (`rolename`);
ALTER TABLE `accounts`.`userrole` 
ADD CONSTRAINT `userrole_ibfk_1` FOREIGN KEY `userrole_ibfk_1` (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `accounts`.`friends` 
ADD CONSTRAINT `friends_ibfk_2` FOREIGN KEY `friends_ibfk_2` (`friend_id`) REFERENCES `users` (`id`);
ALTER TABLE `accounts`.`friends` 
ADD CONSTRAINT `friends_ibfk_1` FOREIGN KEY `friends_ibfk_1` (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `accounts`.`tags` 
ADD CONSTRAINT `tags_ibfk_1` FOREIGN KEY `tags_ibfk_1` (`user_id`) REFERENCES `users` (`id`);
