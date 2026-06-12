-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_digielancer
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `add_on`
--

DROP TABLE IF EXISTS `add_on`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `add_on` (
  `id` int NOT NULL AUTO_INCREMENT,
  `main_service_id` int NOT NULL,
  `addon_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `main_service_id` (`main_service_id`),
  CONSTRAINT `add_on_ibfk_1` FOREIGN KEY (`main_service_id`) REFERENCES `main_service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `add_on`
--

LOCK TABLES `add_on` WRITE;
/*!40000 ALTER TABLE `add_on` DISABLE KEYS */;
INSERT INTO `add_on` VALUES (4,3,'Web Statis',500000.00),(5,3,'Dashboard Admin',3000000.00),(6,3,'Midtrans',3500000.00),(7,7,'2D',200000.00),(8,7,'3D',500000.00),(12,18,'Dashboard Admin',250000.00),(13,19,'Animasi 3D',500000.00);
/*!40000 ALTER TABLE `add_on` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NOT NULL,
  `board_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `is_completion_board` tinyint(1) DEFAULT '0',
  `position_index` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,1,'To-Do','Tasks that need to be started',0,1),(2,1,'In Progress','Tasks currently being worked on',0,2),(3,1,'Done','Completed tasks ready for review',1,3),(32,10,'To-Do','Tugas yang belum dikerjakan',0,1),(33,10,'In Progress','Tugas yang sedang dikerjakan',0,2),(34,10,'Done','Tugas yang sudah selesai',1,3),(35,10,'Optional','Tugas yang tidak terlalu penting',0,4);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NOT NULL,
  `invoice_number` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `total_amount` decimal(15,2) NOT NULL,
  `status` enum('Pending','Paid','Overdue') COLLATE utf8mb4_general_ci DEFAULT 'Pending',
  `template_style` enum('Logo','PlainText') COLLATE utf8mb4_general_ci DEFAULT 'PlainText',
  `generated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_number` (`invoice_number`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,1,'INV-2026-0001',3250000.00,'Pending','Logo','2026-06-11 06:27:04'),(3,10,'INV-2026-0002',1250000.00,'Paid','Logo','2026-06-12 14:10:38'),(4,10,'INV-2026-0003',1000000.00,'Pending','Logo','2026-06-12 15:38:42'),(5,10,'INV-2026-0004',1000000.00,'Pending','Logo','2026-06-12 15:38:56'),(6,10,'INV-2026-0005',1250000.00,'Pending','Logo','2026-06-12 15:49:22');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_item`
--

DROP TABLE IF EXISTS `invoice_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int NOT NULL,
  `item_description` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `snapshot_price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `invoice_id` (`invoice_id`),
  CONSTRAINT `invoice_item_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_item`
--

LOCK TABLES `invoice_item` WRITE;
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
INSERT INTO `invoice_item` VALUES (1,1,'Commercial Video Editing (Base Service)',2500000.00),(2,1,'Custom Mascot Integration (Add-on)',750000.00),(4,3,'Pembuatan Web (Base Service)',1000000.00),(5,3,'Dashboard Admin (Add-on)',250000.00),(6,4,'Pembuatan Web (Base Service)',1000000.00),(7,5,'Pembuatan Web (Base Service)',1000000.00),(8,6,'Pembuatan Web (Base Service)',1000000.00),(9,6,'Dashboard Admin (Add-on)',250000.00);
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `main_service`
--

DROP TABLE IF EXISTS `main_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `main_service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `service_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `base_price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `main_service_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `main_service`
--

LOCK TABLES `main_service` WRITE;
/*!40000 ALTER TABLE `main_service` DISABLE KEYS */;
INSERT INTO `main_service` VALUES (3,2,'Pembuatan Web',100000.00),(4,2,'Aplikasi Mobile',500000.00),(5,2,'Desain Grafis',50000.00),(6,2,'Motion Graphic',80000.00),(7,3,'Motion Graphic',130000.00),(8,1,'Pembuatan Web',500000.00),(9,1,'Java Development',500000.00),(10,1,'Aplikasi Mobile',500000.00),(11,1,'API Integration',500000.00),(18,6,'Pembuatan Web',1000000.00),(19,6,'Motion Graphic',750000.00),(20,6,'Game RPG',850000.00);
/*!40000 ALTER TABLE `main_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `main_service_id` int DEFAULT NULL,
  `client_name` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `client_contact` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `deadline` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `main_service_id` (`main_service_id`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_ibfk_2` FOREIGN KEY (`main_service_id`) REFERENCES `main_service` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,1,NULL,'Exstore Gaming','contact@exstore.com','2026-06-30','2026-06-11 06:27:04',NULL),(10,6,18,'Ansyah Company','','2026-06-30','2026-06-12 14:07:21',1);
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `deadline` date DEFAULT NULL,
  `priority` enum('Low','Medium','High') COLLATE utf8mb4_general_ci DEFAULT 'Medium',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `board_id` (`board_id`),
  CONSTRAINT `task_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,1,'Gather Assets','Download gameplay footage and audio stems from client drive.','2026-06-15','High','2026-06-11 06:27:04'),(2,2,'Rough Cut Assembly','Sync audio and cut out dead air.','2026-06-20','High','2026-06-11 06:27:04'),(3,3,'Scripting UI Panel','Automate repetitive transition placements for the timeline.','2026-06-12','Medium','2026-06-11 06:27:04'),(4,34,'Struktur Folder','Membuat Struktur Folder sesuai Framework','2026-06-12','High','2026-06-12 14:09:30'),(5,33,'Database','Menghubungkan Database','2026-06-14','Medium','2026-06-12 14:12:15');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `business_name` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Creative Edits Studio','hello@creativeedits.com','hashed_password_placeholder','2026-06-11 06:27:04'),(2,'Ansyahmf Creative','firdiansyahmf.04@gmail.com','1234567','2026-06-11 07:23:52'),(3,'Cahya Studio','cahyaaziz@upi.edu','123456','2026-06-11 07:35:59'),(6,'Dwi Company','dwikautsar@gmail.com','12345678','2026-06-12 14:05:43');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'db_digielancer'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12 23:03:19
