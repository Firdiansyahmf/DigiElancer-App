-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 11, 2026 at 10:20 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

CREATE DATABASE IF NOT EXISTS db_digielancer;
USE db_digielancer;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- --------------------------------------------------------
-- 1. Table structure for table `user`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_name` varchar(150) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=4;

-- Data Dump for table `user` (Hanya masuk jika data kosong)
INSERT INTO `user` (`id`, `business_name`, `email`, `password_hash`, `created_at`) 
SELECT 1, 'Creative Edits Studio', 'hello@creativeedits.com', 'hashed_password_placeholder', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `id` = 1);
INSERT INTO `user` (`id`, `business_name`, `email`, `password_hash`, `created_at`) 
SELECT 2, 'Ansyahmf Creative', 'firdiansyahmf.04@gmail.com', '123456', '2026-06-11 07:23:52' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `id` = 2);
INSERT INTO `user` (`id`, `business_name`, `email`, `password_hash`, `created_at`) 
SELECT 3, 'Cahya Studio', 'cahyaaziz@upi.edu', '123456', '2026-06-11 07:35:59' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `id` = 3);


-- --------------------------------------------------------
-- 2. Table structure for table `main_service`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `main_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `base_price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `main_service_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=8;

-- Data Dump for table `main_service`
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 1, 1, 'Commercial Video Editing', 2500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 1);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 2, 1, 'Motion Design / Animation', 3000000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 2);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 3, 2, 'Pembuatan Web', 100000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 3);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 4, 2, 'Aplikasi Mobile', 500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 4);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 5, 2, 'Desain Grafis', 50000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 5);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 6, 2, 'Motion Graphic', 80000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 6);
INSERT INTO `main_service` (`id`, `user_id`, `service_name`, `base_price`)
SELECT 7, 3, 'Motion Graphic', 130000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `main_service` WHERE `id` = 7);


-- --------------------------------------------------------
-- 3. Table structure for table `add_on`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `add_on` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `main_service_id` int(11) NOT NULL,
  `addon_name` varchar(100) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `main_service_id` (`main_service_id`),
  CONSTRAINT `add_on_ibfk_1` FOREIGN KEY (`main_service_id`) REFERENCES `main_service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=9;

-- Data Dump for table `add_on`
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 1, 1, 'Color Grading (Cinematic)', 500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 1);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 2, 1, 'Subtitles / Closed Captions', 250000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 2);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 3, 1, 'Custom Mascot Integration', 750000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 3);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 4, 3, 'Web Statis', 500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 4);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 5, 3, 'Dashboard Admin', 3000000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 5);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 6, 3, 'Midtrans', 3500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 6);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 7, 7, '2D', 200000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 7);
INSERT INTO `add_on` (`id`, `main_service_id`, `addon_name`, `price`)
SELECT 8, 7, '3D', 500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `add_on` WHERE `id` = 8);


-- --------------------------------------------------------
-- 4. Table structure for table `project`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `main_service_id` int(11) DEFAULT NULL,
  `client_name` varchar(150) NOT NULL,
  `client_contact` varchar(150) NOT NULL,
  `deadline` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `main_service_id` (`main_service_id`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_ibfk_2` FOREIGN KEY (`main_service_id`) REFERENCES `main_service` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=4;

-- Data Dump for table `project`
INSERT INTO `project` (`id`, `user_id`, `main_service_id`, `client_name`, `client_contact`, `deadline`, `created_at`)
SELECT 1, 1, 1, 'Exstore Gaming', 'contact@exstore.com', '2026-06-30', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `project` WHERE `id` = 1);
INSERT INTO `project` (`id`, `user_id`, `main_service_id`, `client_name`, `client_contact`, `deadline`, `created_at`)
SELECT 2, 2, 3, 'Exstore Shop', 'contact@exstore.com', '2026-07-15', '2026-06-11 07:23:52' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `project` WHERE `id` = 2);
INSERT INTO `project` (`id`, `user_id`, `main_service_id`, `client_name`, `client_contact`, `deadline`, `created_at`)
SELECT 3, 3, 7, 'Cahya Aziz Client', 'cahya@client.com', '2026-07-20', '2026-06-11 07:35:59' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `project` WHERE `id` = 3);


-- --------------------------------------------------------
-- 5. Table structure for table `board`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `board` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `board_name` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `is_completion_board` tinyint(1) DEFAULT 0,
  `position_index` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=10;

-- Data Dump for table `board`
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 1, 1, 'To-Do', 'Tasks that need to be started', 0, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 1);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 2, 1, 'In Progress', 'Tasks currently being worked on', 0, 2 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 2);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 3, 1, 'Done', 'Completed tasks ready for review', 1, 3 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 3);

-- Boards for Project 2 (Exstore Shop)
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 4, 2, 'To-Do', 'Tasks that need to be started', 0, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 4);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 5, 2, 'In Progress', 'Tasks currently being worked on', 0, 2 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 5);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 6, 2, 'Done', 'Completed tasks ready for review', 1, 3 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 6);

-- Boards for Project 3 (Cahya Aziz Client)
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 7, 3, 'To-Do', 'Tasks that need to be started', 0, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 7);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 8, 3, 'In Progress', 'Tasks currently being worked on', 0, 2 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 8);
INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`)
SELECT 9, 3, 'Done', 'Completed tasks ready for review', 1, 3 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `board` WHERE `id` = 9);


-- --------------------------------------------------------
-- 6. Table structure for table `invoice`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `invoice_number` varchar(50) NOT NULL,
  `total_amount` decimal(15,2) NOT NULL,
  `status` enum('Pending','Paid','Overdue') DEFAULT 'Pending',
  `template_style` enum('Logo','PlainText') DEFAULT 'PlainText',
  `generated_date` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_number` (`invoice_number`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=2;

-- Data Dump for table `invoice`
INSERT INTO `invoice` (`id`, `project_id`, `invoice_number`, `total_amount`, `status`, `template_style`, `generated_date`)
SELECT 1, 1, 'INV-2026-0001', 3250000.00, 'Pending', 'Logo', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `invoice` WHERE `id` = 1);


-- --------------------------------------------------------
-- 7. Table structure for table `invoice_item`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `item_description` varchar(255) NOT NULL,
  `snapshot_price` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `invoice_id` (`invoice_id`),
  CONSTRAINT `invoice_item_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=3;

-- Data Dump for table `invoice_item`
INSERT INTO `invoice_item` (`id`, `invoice_id`, `item_description`, `snapshot_price`)
SELECT 1, 1, 'Commercial Video Editing (Base Service)', 2500000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `invoice_item` WHERE `id` = 1);
INSERT INTO `invoice_item` (`id`, `invoice_id`, `item_description`, `snapshot_price`)
SELECT 2, 1, 'Custom Mascot Integration (Add-on)', 750000.00 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `invoice_item` WHERE `id` = 2);


-- --------------------------------------------------------
-- 8. Table structure for table `task`
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `board_id` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `priority` enum('Low','Medium','High') DEFAULT 'Medium',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `board_id` (`board_id`),
  CONSTRAINT `task_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci AUTO_INCREMENT=4;

-- Data Dump for table `task`
INSERT INTO `task` (`id`, `board_id`, `title`, `description`, `deadline`, `priority`, `created_at`)
SELECT 1, 1, 'Gather Assets', 'Download gameplay footage and audio stems from client drive.', '2026-06-15', 'High', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `task` WHERE `id` = 1);
INSERT INTO `task` (`id`, `board_id`, `title`, `description`, `deadline`, `priority`, `created_at`)
SELECT 2, 2, 'Rough Cut Assembly', 'Sync audio and cut out dead air.', '2026-06-20', 'High', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `task` WHERE `id` = 2);
INSERT INTO `task` (`id`, `board_id`, `title`, `description`, `deadline`, `priority`, `created_at`)
SELECT 3, 3, 'Scripting UI Panel', 'Automate repetitive transition placements for the timeline.', '2026-06-12', 'Medium', '2026-06-11 06:27:04' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `task` WHERE `id` = 3);

COMMIT;

    -- 1. Menambahkan dummy project untuk user_id = 2 (Ansyahmf Creative)                                                                                                                           
    INSERT INTO `project` (`id`, `user_id`, `main_service_id`, `client_name`, `client_contact`, `deadline`, `created_at`)                                                                           
    VALUES (2, 2, 3, 'Exstore Shop', 'contact@exstore.com', '2026-07-15', NOW())                                                                                                                    
    ON DUPLICATE KEY UPDATE id=id;                                                                                                                                                                  
                                                                                                                                                                                                    
    -- 2. Menambahkan dummy project untuk user_id = 3 (Cahya Studio)                                                                                                                                
    INSERT INTO `project` (`id`, `user_id`, `main_service_id`, `client_name`, `client_contact`, `deadline`, `created_at`)                                                                           
    VALUES (3, 3, 7, 'Cahya Aziz Client', 'cahya@client.com', '2026-07-20', NOW())                                                                                                                  
    ON DUPLICATE KEY UPDATE id=id;                                                                                                                                                                  
                                                                                                                                                                                                    
    -- 3. Menambahkan boards wajib untuk Project ID 2 (agar kanban board tidak error)                                                                                                               
    INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`) VALUES                                                                           
    (4, 2, 'To-Do', 'Tasks that need to be started', 0, 1),                                                                                                                                         
    (5, 2, 'In Progress', 'Tasks currently being worked on', 0, 2),                                                                                                                                 
    (6, 2, 'Done', 'Completed tasks ready for review', 1, 3)                                                                                                                                        
    ON DUPLICATE KEY UPDATE id=id;                                                                                                                                                                  
                                                                                                                                                                                                    
    -- 4. Menambahkan boards wajib untuk Project ID 3                                                                                                                                               
    INSERT INTO `board` (`id`, `project_id`, `board_name`, `description`, `is_completion_board`, `position_index`) VALUES                                                                           
    (7, 3, 'To-Do', 'Tasks that need to be started', 0, 1),                                                                                                                                         
    (8, 3, 'In Progress', 'Tasks currently being worked on', 0, 2),                                                                                                                                 
    (9, 3, 'Done', 'Completed tasks ready for review', 1, 3)                                                                                                                                        
    ON DUPLICATE KEY UPDATE id=id;  
