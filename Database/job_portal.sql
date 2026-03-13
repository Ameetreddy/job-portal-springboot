-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: jobportal
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `job_company`
--

DROP TABLE IF EXISTS `job_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_location`
--

DROP TABLE IF EXISTS `job_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_post_activity`
--

DROP TABLE IF EXISTS `job_post_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_seeker_apply`
--

DROP TABLE IF EXISTS `job_seeker_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_seeker_profile`
--

DROP TABLE IF EXISTS `job_seeker_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_seeker_save`
--

DROP TABLE IF EXISTS `job_seeker_save`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pending_users`
--

DROP TABLE IF EXISTS `pending_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recruiter_profile`
--

DROP TABLE IF EXISTS `recruiter_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_type`
--

DROP TABLE IF EXISTS `users_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_type` (
  `user_type_id` int NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `verification_tokens`
--

DROP TABLE IF EXISTS `verification_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-13 11:48:22
