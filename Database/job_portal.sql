
-- Table structure for table `job_company`
--


CREATE TABLE `job_company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `job_location`
--


CREATE TABLE `job_location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `job_post_activity`
--


CREATE TABLE `job_post_activity` (
  `job_post_id` int NOT NULL AUTO_INCREMENT,
  `description_of_job` varchar(10000) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `job_type` varchar(255) DEFAULT NULL,
  `posted_date` datetime(6) DEFAULT NULL,
  `remote` varchar(255) DEFAULT NULL,
  `salary` varchar(255) DEFAULT NULL,
  `job_company_id` int DEFAULT NULL,
  `job_location_id` int DEFAULT NULL,
  `posted_by_id` int DEFAULT NULL,
  PRIMARY KEY (`job_post_id`),
  KEY `FKpjpv059hollr4tk92ms09s6is` (`job_company_id`),
  KEY `FK44003mnvj29aiijhsc6ftsgxe` (`job_location_id`),
  KEY `FK62yqqbypsq2ik34ngtlw4m9k3` (`posted_by_id`),
  CONSTRAINT `FK44003mnvj29aiijhsc6ftsgxe` FOREIGN KEY (`job_location_id`) REFERENCES `job_location` (`id`),
  CONSTRAINT `FK_job_user` FOREIGN KEY (`posted_by_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `FKpjpv059hollr4tk92ms09s6is` FOREIGN KEY (`job_company_id`) REFERENCES `job_company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `job_seeker_apply`
--


CREATE TABLE `job_seeker_apply` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_date` datetime(6) DEFAULT NULL,
  `cover_letter` varchar(255) DEFAULT NULL,
  `job` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pending',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8v6qok40anljlhpkc486nsdmu` (`user_id`,`job`),
  KEY `FKmfhx9q4uclbb74vm49lv9dmf4` (`job`),
  CONSTRAINT `FK_job_apply` FOREIGN KEY (`job`) REFERENCES `job_post_activity` (`job_post_id`) ON DELETE CASCADE,
  CONSTRAINT `FKs9fftlyxws2ak05q053vi57qv` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profile` (`user_account_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `job_seeker_profile`
--


CREATE TABLE `job_seeker_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `employment_type` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `profile_photo` varchar(255) DEFAULT NULL,
  `resume` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `work_authorization` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `FKohp1poe14xlw56yxbwu2tpdm7` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `job_seeker_save`
--


CREATE TABLE `job_seeker_save` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1vn1w4dxfiavb5q2gu1n0whxo` (`user_id`,`job`),
  KEY `FKpb44x040gkdltxqy9m7jmvvf3` (`job`),
  CONSTRAINT `FK96dyvgd8hmdohqsfdpvyl89mg` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profile` (`user_account_id`) ON DELETE CASCADE,
  CONSTRAINT `FKpb44x040gkdltxqy9m7jmvvf3` FOREIGN KEY (`job`) REFERENCES `job_post_activity` (`job_post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `pending_users`
--

DROP TABLE IF EXISTS `pending_users`;

CREATE TABLE `pending_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_type_id` int NOT NULL,
  `token` varchar(255) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `token` (`token`),
  KEY `fk_pending_user_type` (`user_type_id`),
  CONSTRAINT `fk_pending_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `users_type` (`user_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `recruiter_profile`
--


CREATE TABLE `recruiter_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `profile_photo` varchar(64) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `FK_recruiter_user` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `skills`
--


CREATE TABLE `skills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `experience_level` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `years_of_experience` int DEFAULT '0',
  `job_seeker_profile` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsjdksau8sat30c00aqh5xf2wh` (`job_seeker_profile`),
  CONSTRAINT `FKsjdksau8sat30c00aqh5xf2wh` FOREIGN KEY (`job_seeker_profile`) REFERENCES `job_seeker_profile` (`user_account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL,
  `user_type_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FK5snet2ikvi03wd4rabd40ckdl` (`user_type_id`),
  CONSTRAINT `FK5snet2ikvi03wd4rabd40ckdl` FOREIGN KEY (`user_type_id`) REFERENCES `users_type` (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `users_type`
--


CREATE TABLE `users_type` (
  `user_type_id` int NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `verification_tokens`
--

DROP TABLE IF EXISTS `verification_tokens`;

CREATE TABLE `verification_tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  KEY `fk_verification_user` (`user_id`),
  CONSTRAINT `fk_verification_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


