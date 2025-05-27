-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: alpha
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
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `user_id` int NOT NULL,
  `qr_code_id` int NOT NULL,
  `scanned_at` time(6) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`qr_code_id`),
  KEY `user_id` (`user_id`),
  KEY `qr_code_id` (`qr_code_id`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`qr_code_id`) REFERENCES `qr_codes` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (31,5,'22:47:12.819000'),(31,7,'08:12:26.791000'),(31,10,'11:18:43.850000'),(31,12,'18:28:11.591000'),(32,4,'00:28:52.000000'),(32,6,'00:08:48.000000'),(32,7,'08:11:07.066000'),(32,9,'21:49:48.709000'),(41,4,'00:29:03.000000'),(41,10,'11:40:45.366000'),(47,4,'00:31:15.000000'),(48,5,'21:45:04.194000'),(48,7,'08:15:32.907000'),(48,11,'23:15:47.727000'),(49,7,'09:02:26.168000'),(49,8,'09:05:10.967000'),(49,9,'21:42:33.762000'),(50,9,'21:48:30.638000'),(51,9,'22:03:01.904000'),(53,11,'23:04:58.311000'),(56,13,'09:58:50.913000'),(57,12,'18:20:54.764000');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bonus_type`
--

DROP TABLE IF EXISTS `bonus_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bonus_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `points` int NOT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `active_from` datetime DEFAULT NULL,
  `active_to` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bonus_type`
--

LOCK TABLES `bonus_type` WRITE;
/*!40000 ALTER TABLE `bonus_type` DISABLE KEYS */;
INSERT INTO `bonus_type` VALUES (1,'Attendance',50,1,'2025-05-06 19:10:16',NULL),(2,'High JPA',150,1,'2025-05-06 19:10:16',NULL),(3,'Make Diet',50,1,'2025-05-06 19:10:16',NULL),(4,'Active in Meeting',10,1,'2025-05-06 19:10:16',NULL);
/*!40000 ALTER TABLE `bonus_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `church`
--

DROP TABLE IF EXISTS `church`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `church` (
  `id` int NOT NULL AUTO_INCREMENT,
  `church_name` varchar(255) NOT NULL,
  `town_or_village` char(1) DEFAULT NULL,
  `diocese_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_church_diocese` (`diocese_id`),
  CONSTRAINT `fk_church_diocese` FOREIGN KEY (`diocese_id`) REFERENCES `dioceses` (`id`),
  CONSTRAINT `church_chk_1` CHECK ((`town_or_village` in (_utf8mb4'T',_utf8mb4'V')))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `church`
--

LOCK TABLES `church` WRITE;
/*!40000 ALTER TABLE `church` DISABLE KEYS */;
INSERT INTO `church` VALUES (1,'St. George\'s Church, Quesna','T',1),(2,'كنيسة الملاك','T',1),(3,'كنيسة العذراء والاباء الرسل','T',1);
/*!40000 ALTER TABLE `church` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dioceses`
--

DROP TABLE IF EXISTS `dioceses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dioceses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dioceses`
--

LOCK TABLES `dioceses` WRITE;
/*!40000 ALTER TABLE `dioceses` DISABLE KEYS */;
INSERT INTO `dioceses` VALUES (1,'Benha and Qwesna');
/*!40000 ALTER TABLE `dioceses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_dtl`
--

DROP TABLE IF EXISTS `event_dtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_dtl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(150) NOT NULL,
  `description` text,
  `is_paid` tinyint(1) DEFAULT '0',
  `price` int DEFAULT NULL,
  `image` longblob,
  `image_url` varchar(500) DEFAULT NULL,
  `max_spen_point` int DEFAULT '0',
  `event_type_id` int DEFAULT NULL,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_event_type` (`event_type_id`),
  CONSTRAINT `fk_event_type` FOREIGN KEY (`event_type_id`) REFERENCES `event_type` (`event_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_dtl`
--

LOCK TABLES `event_dtl` WRITE;
/*!40000 ALTER TABLE `event_dtl` DISABLE KEYS */;
INSERT INTO `event_dtl` VALUES (1,'DAHB','الفرحة لينا... والمكان مستنينا',1,1700,NULL,'dahb.jpg',200,1,'2025-09-13','2025-09-17'),(2,'ElSokhna',' وتعالي وانتظم علشان لو مش معاك 100 وزنة هتقعد في البيت مش في سخنة ',1,300,NULL,'ElSokhna.jpg',70,1,'2025-07-10','2025-07-10');
/*!40000 ALTER TABLE `event_dtl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_type`
--

DROP TABLE IF EXISTS `event_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_type` (
  `event_type_id` int NOT NULL AUTO_INCREMENT,
  `event_name` varchar(150) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`event_type_id`),
  UNIQUE KEY `event_name` (`event_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_type`
--

LOCK TABLES `event_type` WRITE;
/*!40000 ALTER TABLE `event_type` DISABLE KEYS */;
INSERT INTO `event_type` VALUES (1,'Trip',1);
/*!40000 ALTER TABLE `event_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetings`
--

DROP TABLE IF EXISTS `meetings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `id_church` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_church` (`id_church`),
  CONSTRAINT `meetings_ibfk_1` FOREIGN KEY (`id_church`) REFERENCES `church` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetings`
--

LOCK TABLES `meetings` WRITE;
/*!40000 ALTER TABLE `meetings` DISABLE KEYS */;
INSERT INTO `meetings` VALUES (1,'Mar Girgis Youth Meeting',1,1),(2,'اجتماع شباب الملاك',1,2),(3,'اجتماع خدمة اعدادي العذراء والاباء الرسل',1,3);
/*!40000 ALTER TABLE `meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile` (
  `profile_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `gendre` enum('Male','Female') NOT NULL,
  `service_stage_id` int DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `birth_day` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `father_periest` varchar(100) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `join_date` datetime(6) DEFAULT NULL,
  `profile_image_path_server` varchar(300) DEFAULT NULL,
  `church_id` int DEFAULT NULL,
  `meeting_id` int DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  KEY `fk_service_stage` (`service_stage_id`),
  KEY `fk_church` (`church_id`),
  KEY `fk_meeting` (`meeting_id`),
  CONSTRAINT `FK5t6wj5h99fn9sy076jjl504gy` FOREIGN KEY (`service_stage_id`) REFERENCES `service_stage` (`id`),
  CONSTRAINT `fk_church` FOREIGN KEY (`church_id`) REFERENCES `church` (`id`),
  CONSTRAINT `fk_meeting` FOREIGN KEY (`meeting_id`) REFERENCES `meetings` (`id`),
  CONSTRAINT `fk_service_stage` FOREIGN KEY (`service_stage_id`) REFERENCES `service_stage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` VALUES (50,'isaac','hanna','Male',1,'01205214283','1997-07-13','30 شارع الميهلمي قويسنا','ابونا مرقوريوس','','2025-05-04 22:12:28.531956','0b23a33b-fc5e-46e1-a63d-b930d17cc85a_1728837474039.jpeg',1,1),(51,'Ibrahim','Fakhery','Male',1,'01095125678','2001-05-08','30 شارع الميهلمي','ابونا مرقوريوس','','2025-05-06 16:55:52.562317','2ec76abe-6f1b-47a3-8ac2-7502885635f5_1728837474039.jpeg',1,1),(64,'David','Amir','Male',1,'01286814949','2003-02-04','شارع نجم الدين تقطاع شارع صديقي ','ابونا مرقوريوس','','2025-05-09 13:10:34.444922','55f5028c-189a-4815-82c0-1eb0eaab089a_qrcode_203406236_2650b156fb87d9ee19a6282d845ee30e.jpg',1,1),(70,'Marvina','Mena','Female',1,'01205214286','1963-05-09','30 st el mehalmy','ابونا يؤانس','','2025-05-09 14:16:06.989125','de2101e0-9aec-40bf-92e0-cea797a9e8ee_1000214753.jpg',1,1),(71,'Monica','Saad','Female',1,'01234567890','1997-05-13','30 شارع الميهلمي','ابونا مرقوريوس','','2025-05-10 21:38:49.514740','b9faad39-e65c-40ef-8211-b887a2732864_1728837474039.jpeg',1,1),(72,'Isaac ','Hanna','Male',1,'01234567891','1997-05-31','Mehalmy','ابونا جورجيوس','','2025-05-11 08:52:55.546530','1e92d8f4-dc33-4de2-9f1a-878d1531954a_1728837474039.jpeg',1,1),(73,'Nader','Nagy','Male',1,'01205214282','1997-07-13','30 st el mehalmy','ابونا باخوميوس','','2025-05-17 21:35:59.746918','612c3339-5dae-4ef5-9691-1a1cc4d77ed9_1000217163.jpg',1,1),(74,'Felopater','George','Male',1,'01205214288','1997-05-06','30 st el mehalmy','ابونا يؤانس','','2025-05-17 21:59:42.366327','988daa8a-5f9a-481a-9142-03e26a967d7b_1728837474039.jpeg',1,1),(75,'Nayer','Nader','Male',1,'01205214210','2000-05-29','Mehalmy','ابونا يؤانس','','2025-05-17 22:33:54.836611','fabeea8d-4a4c-4c64-b50a-a35fd5dcae20_1728837474039.jpeg',1,1),(76,'Joseph','Medhat','Male',1,'01274139655','1998-06-28','بجوار مركز الشرطة جانب مستشفي الصفوة','ابونا يوسف','','2025-05-19 21:56:57.490739','8ca46625-8483-4333-8972-9211a0fb547f_jos.jpg',1,1),(77,'سيدنا','الانبامكسيموس','Male',1,'01555555555','1954-05-25','الكنيسة','ابونا بنيامين','','2025-05-25 21:39:44.861134','644453c1-a42f-4d0a-9f83-47bb9fed9d7c_032e3973-c2ef-4690-8f77-d45efefef78f.jpeg',2,2),(78,'Abnoub','Wakeem','Male',1,'01284714133','1993-05-25','شارع صديقي','ابونا يوسف','','2025-05-25 21:50:53.276083','9efd8f9a-180b-4f55-ab9b-400956816059_abnoub.jpg',2,2),(79,'Peter','Gerges','Male',1,'01205214444','1988-05-29','مهجر شارع السوق القديم','ابونا يؤانس','','2025-05-26 17:24:39.108119','d880c6f0-bfd8-4cff-9ea7-97a4e2ec9105_495454031_24408970905370472_6729412655494728208_n.jpg',3,3),(80,'Marina','Saied','Female',1,'01205214777','1997-05-14','شارع الميهلمي','ابونا يؤانس','','2025-05-26 18:14:16.520758','70185530-a02e-4f69-80b9-ec1b5ad37c32_143976809_2935649896665770_7519823465114993253_n.jpg',3,3);
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_status`
--

DROP TABLE IF EXISTS `purchase_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` enum('pending','approved','cancelled') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_status`
--

LOCK TABLES `purchase_status` WRITE;
/*!40000 ALTER TABLE `purchase_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `purchase_id` int NOT NULL AUTO_INCREMENT,
  `profile_id` int NOT NULL,
  `event_id` int DEFAULT NULL,
  `points_spent` int NOT NULL,
  `purchase_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status_id` int NOT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `status_id` (`status_id`),
  KEY `fk_purchases_profile` (`profile_id`),
  KEY `fk_purchases_event` (`event_id`),
  CONSTRAINT `fk_purchases_event` FOREIGN KEY (`event_id`) REFERENCES `event_dtl` (`id`),
  CONSTRAINT `fk_purchases_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`),
  CONSTRAINT `purchases_ibfk_1` FOREIGN KEY (`status_id`) REFERENCES `purchase_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qr_codes`
--

DROP TABLE IF EXISTS `qr_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qr_codes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `valid_date` date NOT NULL,
  `valid_start` time NOT NULL,
  `valid_end` time NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `meeting_id` int DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qr_codes`
--

LOCK TABLES `qr_codes` WRITE;
/*!40000 ALTER TABLE `qr_codes` DISABLE KEYS */;
INSERT INTO `qr_codes` VALUES (1,'EVENT2025','2025-05-07','19:00:00','23:59:00','Evening youth event','2025-05-06 16:10:16',1,0),(2,'EVENT003','2025-05-09','09:26:25','12:00:00','Test event in PostgreSQL','2025-05-09 06:26:25',1,0),(3,'EVENt004','2025-05-09','13:00:00','13:10:00','Test','2025-05-09 10:12:26',1,0),(4,'EVENt005','2025-05-10','00:00:00','01:00:00','Test','2025-05-09 10:12:26',1,0),(5,'EVENT006','2025-05-10','21:00:00','23:59:00','Test','2025-05-09 10:12:26',1,0),(6,'EVENT007','2025-05-11','00:00:00','01:00:00','Test','2025-05-09 10:12:26',1,0),(7,'EVENT008','2025-05-11','09:00:00','10:00:00','Test','2025-05-09 10:12:26',1,0),(8,'EVENT009','2025-05-11','09:00:00','10:00:00','Test','2025-05-09 10:12:26',1,1),(9,'EVENT0010','2025-05-17','21:40:00','23:00:00','Test','2025-05-09 10:12:26',1,1),(10,'EVENT0011','2025-05-18','11:17:00','12:00:00','test','2025-05-18 08:15:01',1,1),(11,'EVENT12','2025-05-19','23:00:00','23:59:00','TEST','2025-05-19 20:01:56',1,1),(12,'EVENT13','2025-05-26','18:00:00','18:59:00',NULL,'2025-05-26 15:16:57',3,1),(13,'EVENT15','2025-05-27','09:00:00','10:00:00','test','2025-05-27 06:40:44',3,1);
/*!40000 ALTER TABLE `qr_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_YOUTH'),(2,'ROLE_SERVER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_stage`
--

DROP TABLE IF EXISTS `service_stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_stage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_stage`
--

LOCK TABLES `service_stage` WRITE;
/*!40000 ALTER TABLE `service_stage` DISABLE KEYS */;
INSERT INTO `service_stage` VALUES (1,'university stage');
/*!40000 ALTER TABLE `service_stage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sprint_data`
--

DROP TABLE IF EXISTS `sprint_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sprint_data` (
  `sprint_id` int NOT NULL AUTO_INCREMENT,
  `from_date` date NOT NULL,
  `to_date` date NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `point_price` double(16,8) DEFAULT NULL,
  `church_id` int DEFAULT NULL,
  `meeting_id` int DEFAULT NULL,
  PRIMARY KEY (`sprint_id`),
  KEY `fk_meeting_id` (`meeting_id`),
  CONSTRAINT `fk_meeting_id` FOREIGN KEY (`meeting_id`) REFERENCES `meetings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sprint_data`
--

LOCK TABLES `sprint_data` WRITE;
/*!40000 ALTER TABLE `sprint_data` DISABLE KEYS */;
INSERT INTO `sprint_data` VALUES (1,'2025-05-09','2025-09-15',1,0.20000000,1,1),(2,'2025-05-09','2025-09-15',1,0.20000000,2,2),(3,'2025-05-09','2025-09-15',1,0.20000000,3,3);
/*!40000 ALTER TABLE `sprint_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
  `config_key` varchar(100) NOT NULL,
  `config_value` varchar(100) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES ('profile.edit.church-meeting','false','Flag For Edit Church And Meeting');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `is_enabled` tinyint(1) DEFAULT '1',
  `is_locked` tinyint(1) DEFAULT '0',
  `last_login` datetime(6) DEFAULT NULL,
  `profile_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `UK1mcjtpxmwom9h9bf2q0k412e0` (`profile_id`),
  KEY `fk_user_profile` (`profile_id`),
  CONSTRAINT `fk_user_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`),
  CONSTRAINT `FKof44u64o1d7scaukghm9veo23` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (31,'01205214283','$2a$12$XTUPpI45YH5r7KXB2AJNGOKMUKotuPdj47EOLSo2gpRPQXI206RRu',1,0,'2025-05-04 22:12:28.520945',50),(32,'01095125678','$2a$12$0oCJWSCiJ0dvA0zxRgTei.R.uXTQ9JWhuFh9NHevTAQT1VL1rytrS',1,0,'2025-05-06 16:55:52.550245',51),(41,'01286814949','$2a$12$GmSgmSkQU1xJEOZRttX3tOg8OiqWQhNNm1fMlg8zInBZbomJp2j26',1,0,'2025-05-09 13:10:34.433874',64),(47,'01205214286','$2a$12$18dfS.dMlIGDM8H1e9AGa.p7FgfecyQirKfvDJ59CDWMgMrJyh6Ju',1,0,'2025-05-09 14:16:06.979837',70),(48,'01234567890','$2a$12$DdQvTspe.CxGI60MAXGY1ecJyV4sUiDo4sn98rC4D5LnHxGGB5rt.',1,0,'2025-05-10 21:38:49.496771',71),(49,'01234567891','$2a$12$jxQh8D72my2yDqCZQQRW3uCBjJyuI8Jz7CEaihEKQ7BWJLzeCvDam',1,0,'2025-05-11 08:52:55.539674',72),(50,'01205214282','$2a$12$rBWc7KWaMnxJEmlk6YDJju3DfzNtVF1PH4azbQ28nPJ9p8qGThEuG',1,0,'2025-05-17 21:35:59.730976',73),(51,'01205214288','$2a$12$aR7lT79jXpzlfjBCLutfO.nj9p6ocaFyjsuFWxf8eaYU0Soosj0hu',1,0,'2025-05-17 21:59:42.343079',74),(52,'01205214210','$2a$12$Bw0nSqcdAn84x11S7zxHze7BXH2gIAeYbat6NbqockuIjnNK.V/Hu',1,0,'2025-05-17 22:33:54.820362',75),(53,'01274139655','$2a$12$wLseTual5R6UED5Rn/63muUCoHl.XM2Rpk1WWk2S.VZotOi1Zd1Dm',0,0,'2025-05-19 21:56:57.471304',76),(54,'01555555555','$2a$12$mN91GPIx67UrwqiOs9AuauX1h3xuiDunM9xP9fUUdqkx5LRAYuth2',1,0,'2025-05-25 21:39:44.853480',77),(55,'01284714133','$2a$12$twkFp7HKBWcFvWUnBw/6WuyJNYHFK8iLz2oSgktkEG2a6Oqo0d1CC',1,0,'2025-05-25 21:50:53.248103',78),(56,'01205214444','$2a$12$RkIDc7QZ9QO0SuNnyO4kOuczxINjcLF43MK5HYeCB2GGxpPZmLycu',1,0,'2025-05-26 17:24:39.085453',79),(57,'01205214777','$2a$12$7xCZ7UFEwL1ZCEeyinXxdutA4vXPIsK8wHMsdSb0v1UWpuEXj.z3y',1,0,'2025-05-26 18:14:16.509756',80);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_bonus`
--

DROP TABLE IF EXISTS `user_bonus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_bonus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `profile_id` int NOT NULL,
  `bonus_type_id` int NOT NULL,
  `bounce_point` double DEFAULT NULL,
  `sprint_id` int NOT NULL,
  `added_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `add_by_user_id` int DEFAULT NULL,
  `point_price` double DEFAULT NULL,
  `bonce_type_point` int DEFAULT NULL,
  `meeting_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `bonus_type_id` (`bonus_type_id`),
  KEY `sprint_id` (`sprint_id`),
  KEY `fk_user_bonus_profile` (`profile_id`),
  KEY `fk_user_bonus_added_by_user` (`add_by_user_id`),
  CONSTRAINT `fk_user_bonus_added_by_user` FOREIGN KEY (`add_by_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_user_bonus_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`),
  CONSTRAINT `user_bonus_ibfk_1` FOREIGN KEY (`bonus_type_id`) REFERENCES `bonus_type` (`id`),
  CONSTRAINT `user_bonus_ibfk_2` FOREIGN KEY (`sprint_id`) REFERENCES `sprint_data` (`sprint_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_bonus`
--

LOCK TABLES `user_bonus` WRITE;
/*!40000 ALTER TABLE `user_bonus` DISABLE KEYS */;
INSERT INTO `user_bonus` VALUES (1,70,1,25,1,'2025-05-10 00:21:10',47,0.2,50,1),(2,50,1,25,1,'2025-05-10 00:23:43',31,0.2,50,1),(3,51,1,25,1,'2025-05-10 00:28:52',32,0.2,50,1),(4,64,1,25,1,'2025-05-10 00:29:03',41,0.2,50,1),(5,70,1,12,1,'2025-05-10 00:31:15',47,0.2,50,1),(6,71,1,25,1,'2025-05-10 21:45:04',48,0.2,50,1),(7,50,1,12,1,'2025-05-10 22:47:13',31,0.2,50,1),(8,64,1,0,1,'2025-05-10 23:44:02',41,0.2,50,1),(9,51,1,50,1,'2025-05-11 00:08:49',32,0.2,50,1),(10,51,1,50,1,'2025-05-11 08:11:07',32,0.2,50,1),(11,50,1,50,1,'2025-05-11 08:12:27',31,0.2,50,1),(12,71,1,25,1,'2025-05-11 08:15:33',48,0.2,50,1),(14,72,1,0,1,'2025-05-11 08:58:06',49,0.2,50,1),(15,72,1,50,1,'2025-05-11 09:02:26',49,0.2,50,1),(16,72,1,50,1,'2025-05-11 09:05:11',49,0.2,50,1),(17,72,1,50,1,'2025-05-17 21:42:34',49,0.2,50,1),(18,73,1,50,1,'2025-05-17 21:48:31',50,0.2,50,1),(19,51,1,50,1,'2025-05-17 21:49:49',32,0.2,50,1),(20,74,1,25,1,'2025-05-17 22:03:02',51,0.2,50,1),(21,50,1,50,1,'2025-05-18 11:18:44',31,0.2,50,1),(22,64,1,12,1,'2025-05-18 11:40:45',41,0.2,50,1),(23,76,1,50,1,'2025-05-19 23:04:58',53,0.2,50,1),(24,71,1,25,1,'2025-05-19 23:15:48',48,0.2,50,1),(25,50,2,150,1,'2025-05-25 20:28:59',31,0.2,150,1),(26,50,2,150,1,'2025-05-25 20:30:48',31,0.2,150,1),(27,50,2,150,1,'2025-05-25 20:37:00',31,0.2,150,1),(28,50,1,50,1,'2025-05-25 20:39:47',31,0.2,50,1),(29,50,2,150,1,'2025-05-25 20:40:13',31,0.2,150,1),(30,50,3,50,1,'2025-05-25 20:41:30',31,0.2,50,1),(31,50,4,10,1,'2025-05-25 20:43:07',31,0.2,10,1),(32,50,4,10,1,'2025-05-25 20:44:38',31,0.2,10,1),(33,50,4,10,1,'2025-05-25 20:46:40',31,0.2,10,1),(34,50,3,50,1,'2025-05-25 20:48:13',31,0.2,50,1),(35,50,4,10,1,'2025-05-25 21:30:46',31,0.2,10,1),(36,64,1,50,1,'2025-05-25 22:10:36',41,0.2,50,1),(37,64,2,150,1,'2025-05-25 22:10:59',41,0.2,150,1),(38,64,2,150,1,'2025-05-25 22:11:31',41,0.2,150,1),(39,64,2,150,1,'2025-05-25 22:11:42',41,0.2,150,1),(40,64,2,150,1,'2025-05-25 22:11:46',41,0.2,150,1),(41,77,2,150,2,'2025-05-25 22:20:54',54,0.2,150,2),(42,77,2,150,2,'2025-05-25 22:21:13',54,0.2,150,2),(43,77,2,150,2,'2025-05-25 22:22:01',54,0.2,150,2),(44,78,4,10,2,'2025-05-25 22:25:05',55,0.2,10,2),(45,80,1,25,3,'2025-05-26 18:20:55',57,0.2,50,3),(46,50,1,12,1,'2025-05-26 18:28:12',31,0.2,50,3),(47,79,4,10,3,'2025-05-26 21:07:36',56,0.2,10,3),(48,79,4,10,3,'2025-05-26 21:08:00',56,0.2,10,3),(49,79,4,10,3,'2025-05-26 21:09:28',56,0.2,10,3),(50,79,1,0,3,'2025-05-27 09:58:51',56,0.2,50,3);
/*!40000 ALTER TABLE `user_bonus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_point_transaction`
--

DROP TABLE IF EXISTS `user_point_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_point_transaction` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `profile_id` int NOT NULL,
  `related_user_id` int DEFAULT NULL,
  `sprint_id` int NOT NULL,
  `church_id` int DEFAULT NULL COMMENT 'Reference to church where points were earned',
  `meeting_id` int DEFAULT NULL COMMENT 'Reference to meetiuser_point_transactionng where points were earned',
  `points` double NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `transaction_date` datetime NOT NULL,
  `transaction_type` varchar(50) NOT NULL,
  `used_for` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `profile_id` (`profile_id`),
  KEY `related_user_id` (`related_user_id`),
  KEY `sprint_id` (`sprint_id`),
  KEY `transaction_type` (`transaction_type`),
  KEY `fk_upt_church` (`church_id`),
  KEY `fk_upt_meeting` (`meeting_id`),
  CONSTRAINT `fk_upt_church` FOREIGN KEY (`church_id`) REFERENCES `church` (`id`),
  CONSTRAINT `fk_upt_meeting` FOREIGN KEY (`meeting_id`) REFERENCES `meetings` (`id`),
  CONSTRAINT `user_point_transaction_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`),
  CONSTRAINT `user_point_transaction_ibfk_2` FOREIGN KEY (`related_user_id`) REFERENCES `profile` (`profile_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_point_transaction`
--

LOCK TABLES `user_point_transaction` WRITE;
/*!40000 ALTER TABLE `user_point_transaction` DISABLE KEYS */;
INSERT INTO `user_point_transaction` VALUES (1,71,NULL,1,1,1,25,1,'2025-05-11 08:15:33','Earn','Attendance Point'),(2,72,NULL,1,1,1,0,1,'2025-05-11 08:53:29','Earn','Attendance Point'),(3,72,NULL,1,1,1,0,1,'2025-05-11 08:58:06','Earn','Attendance Point'),(4,72,NULL,1,1,1,50,1,'2025-05-11 09:02:26','Earn','Attendance Point'),(5,72,NULL,1,1,1,50,1,'2025-05-11 09:05:11','Earn','Attendance Point'),(6,72,NULL,1,1,1,50,1,'2025-05-17 21:42:34','Earn','Attendance Point'),(7,73,NULL,1,1,1,50,1,'2025-05-17 21:48:31','Earn','Attendance Point'),(8,51,NULL,1,1,1,50,1,'2025-05-17 21:49:49','Earn','Attendance Point'),(9,74,NULL,1,1,1,25,1,'2025-05-17 22:03:02','Earn','Attendance Point'),(10,50,NULL,1,1,1,50,1,'2025-05-18 11:18:44','Earn','Attendance Point'),(11,64,NULL,1,1,1,12,1,'2025-05-18 11:40:45','Earn','Attendance Point'),(12,76,NULL,1,1,1,50,1,'2025-05-19 23:04:58','Earn','Attendance Point'),(13,71,NULL,1,1,1,25,1,'2025-05-19 23:15:48','Earn','Attendance Point'),(14,50,NULL,1,1,1,150,1,'2025-05-25 20:28:59','Earn','High JPA'),(15,50,NULL,1,1,1,150,1,'2025-05-25 20:30:48','Earn','High JPA'),(16,50,NULL,1,1,1,150,1,'2025-05-25 20:37:00','Earn','High JPA'),(17,51,NULL,1,1,1,50,1,'2025-05-25 20:39:47','Earn','Attendance'),(18,64,NULL,1,1,1,150,1,'2025-05-25 20:40:13','Earn','High JPA'),(19,73,NULL,1,1,1,50,1,'2025-05-25 20:41:30','Earn','Make Diet'),(20,50,NULL,1,1,1,10,1,'2025-05-25 20:43:07','Earn','Active in Meeting'),(21,73,NULL,1,1,1,10,1,'2025-05-25 20:44:38','Earn','Active in Meeting'),(22,76,NULL,1,1,1,10,1,'2025-05-25 20:46:40','Earn','Active in Meeting'),(23,74,NULL,1,1,1,50,1,'2025-05-25 20:48:13','Earn','Make Diet'),(24,64,NULL,1,1,1,10,1,'2025-05-25 21:30:46','Earn','Active in Meeting'),(25,71,NULL,1,1,1,50,1,'2025-05-25 22:10:36','Earn','Attendance'),(26,71,NULL,1,1,1,150,1,'2025-05-25 22:10:59','Earn','High JPA'),(27,64,NULL,1,1,1,150,1,'2025-05-25 22:11:31','Earn','High JPA'),(28,64,NULL,1,1,1,150,1,'2025-05-25 22:11:42','Earn','High JPA'),(29,64,NULL,1,1,1,150,1,'2025-05-25 22:11:46','Earn','High JPA'),(30,78,NULL,2,2,2,150,1,'2025-05-25 22:20:54','Earn','High JPA'),(31,78,NULL,2,2,2,150,1,'2025-05-25 22:21:13','Earn','High JPA'),(32,77,NULL,2,2,2,150,1,'2025-05-25 22:22:01','Earn','High JPA'),(33,77,NULL,2,2,2,10,1,'2025-05-25 22:25:05','Earn','Active in Meeting'),(34,80,NULL,3,3,3,25,1,'2025-05-26 18:20:55','Earn','Attendance Point'),(35,50,NULL,1,1,1,12,1,'2025-05-26 18:28:12','Earn','Attendance Point'),(36,80,NULL,3,3,3,10,1,'2025-05-26 21:07:36','Earn','Active in Meeting'),(37,80,NULL,3,3,3,10,1,'2025-05-26 21:08:00','Earn','Active in Meeting'),(38,79,NULL,3,3,3,10,1,'2025-05-26 21:09:28','Earn','Active in Meeting'),(39,79,NULL,3,3,3,0,1,'2025-05-27 09:58:51','Earn','Attendance Point');
/*!40000 ALTER TABLE `user_point_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (2,31),(1,32),(1,41),(1,47),(1,48),(1,49),(1,50),(1,51),(1,52),(1,53),(1,54),(1,55),(1,56),(1,57);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-27 12:38:32
