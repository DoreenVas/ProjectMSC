-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: projectmsc
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `game`
--

DROP TABLE IF EXISTS `game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `game` (
  `game_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_type` varchar(20) DEFAULT NULL,
  `num_recognized_buttons` int(11) NOT NULL DEFAULT '0',
  `game_date` datetime DEFAULT NULL,
  `time_limit` int(11) NOT NULL DEFAULT '60',
  `dominant_hand` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` VALUES (1,'Both',0,'2019-03-19 00:20:09',10,NULL),(2,'Both',0,'2019-03-19 00:32:17',10,NULL),(3,'Both',0,'2019-03-19 00:47:30',10,NULL),(4,'Both',0,'2019-03-19 01:21:18',10,NULL),(5,'Both',0,'2019-03-19 01:30:57',10,NULL),(6,'Both',0,'2019-03-19 01:34:11',10,NULL),(7,'Both',0,'2019-03-19 01:40:23',10,NULL),(8,'Both',0,'2019-03-19 01:43:13',10,NULL),(9,'Both',0,'2019-03-19 01:52:54',10,NULL),(10,'Both',0,'2019-03-19 01:57:59',10,NULL),(11,'Both',0,'2019-03-20 01:10:21',10,NULL),(12,'Both',0,'2019-03-20 01:10:33',10,NULL),(13,'Both',0,'2019-03-20 11:34:28',10,NULL),(14,'Both',0,'2019-03-20 11:43:12',10,NULL),(15,'Both',0,'2019-03-20 11:44:15',10,NULL),(16,'Shapes',0,'2019-03-20 17:59:35',10,NULL),(17,'Shapes',0,'2019-03-20 18:04:52',10,NULL),(18,'Shapes',0,'2019-03-20 18:08:53',10,NULL),(19,'Shapes',0,'2019-03-20 19:31:55',10,NULL),(20,'Shapes',0,'2019-03-20 19:35:00',10,NULL),(21,'Shapes',0,'2019-03-20 19:43:19',10,NULL),(22,'Shapes',0,'2019-03-20 22:35:55',10,NULL),(23,'Shapes',0,'2019-03-20 22:36:06',10,NULL),(24,'Shapes',0,'2019-03-20 22:36:17',10,NULL),(25,'Shapes',0,'2019-03-20 22:36:28',10,NULL),(26,'Shapes',0,'2019-03-20 22:45:58',10,NULL),(27,'Shapes',0,'2019-03-20 22:59:20',10,NULL),(28,'Shapes',0,'2019-03-20 23:04:15',10,NULL),(29,'Shapes',0,'2019-03-21 20:05:32',1,NULL),(30,'Shapes',0,'2019-03-25 13:39:43',10,NULL),(31,'Both',0,'2019-03-25 13:45:35',10,NULL),(32,'Both',0,'2019-03-25 13:52:16',10,NULL),(33,'Textures',0,'2019-03-25 13:55:15',10,NULL),(34,'Shapes',0,'2019-03-26 16:11:22',10,NULL),(35,'Shapes',0,'2019-03-26 16:12:21',30,NULL),(36,'Shapes',0,'2019-03-26 16:32:46',45,NULL),(37,'Shapes',0,'2019-03-26 16:40:10',30,NULL),(38,'Shapes',2,'2019-03-28 00:51:20',1,'כן'),(39,'Shapes',4,'2019-03-28 00:57:05',1,'לא'),(40,'Textures',0,'2019-03-28 01:00:02',1,'כן'),(41,'Shapes',0,'2019-03-28 01:04:39',1,'כן'),(42,'Shapes',0,'2019-03-28 01:06:06',1,'כן'),(43,'Shapes',0,'2019-03-28 01:08:55',1,'כן');
/*!40000 ALTER TABLE `game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `patient` (
  `patient_id` varchar(20) NOT NULL DEFAULT '0',
  `patient_name` varchar(100) DEFAULT NULL,
  `patient_gender` varchar(1) DEFAULT NULL,
  `dominant_hand` varchar(1) DEFAULT NULL,
  `birth_date` date NOT NULL,
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES ('111111111','asd asd','M','L','2019-02-04'),('111111221','שרה רביבו','F','R','1953-03-03'),('112233445','רו רו','M','R','1971-02-03'),('121212121',' ','F','R','2019-02-19'),('2','asd','F','R','1993-06-13'),('222222222','qwe qwe','M','R','2019-01-31'),('3','asd','F','R','1993-06-13'),('302222222','דורין וסרמן','F','L','2019-02-11'),('302222275','דורין וסרמן','M','L','1992-03-12'),('302228275','נדב שפיצר','M','L','1992-12-03'),('303030303','שמעון כהן','M','R','2019-02-01'),('315383133','שמעון כהן','M','L','2019-01-28'),('32','asd','F','R','1990-01-01'),('333333333','zxc zxc','M','R','2019-01-31'),('4','','F','R','1993-06-13'),('444444444','שמעון כהן','M','R','2018-02-12');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_game`
--

DROP TABLE IF EXISTS `patient_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `patient_game` (
  `patient_id` varchar(20) NOT NULL,
  `game_id` int(11) NOT NULL,
  KEY `game_id_fk_idx` (`game_id`),
  KEY `patient_id_fk_idx` (`patient_id`),
  CONSTRAINT `game_id_fk` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`),
  CONSTRAINT `patient_id_fk` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_game`
--

LOCK TABLES `patient_game` WRITE;
/*!40000 ALTER TABLE `patient_game` DISABLE KEYS */;
INSERT INTO `patient_game` VALUES ('2',30),('2',31),('2',32),('2',33),('2',34),('2',35),('2',36),('2',37),('302228275',38),('302228275',39),('302228275',40),('302228275',41),('302228275',42),('302228275',43);
/*!40000 ALTER TABLE `patient_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shapes`
--

DROP TABLE IF EXISTS `shapes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `shapes` (
  `game_id` int(11) NOT NULL,
  `arrow` decimal(5,2) DEFAULT '-1.00',
  `rectangle` decimal(5,2) DEFAULT '-1.00',
  `diamond` decimal(5,2) DEFAULT '-1.00',
  `pie` decimal(5,2) DEFAULT '-1.00',
  `triangle` decimal(5,2) DEFAULT '-1.00',
  `heart` decimal(5,2) DEFAULT '-1.00',
  `flower` decimal(5,2) DEFAULT '-1.00',
  `hexagon` decimal(5,2) DEFAULT '-1.00',
  `moon` decimal(5,2) DEFAULT '-1.00',
  `plus` decimal(5,2) DEFAULT '-1.00',
  `oval` decimal(5,2) DEFAULT '-1.00',
  `two_triangles` decimal(5,2) DEFAULT '-1.00',
  `circle` decimal(5,2) DEFAULT '-1.00',
  `star` decimal(5,2) DEFAULT '-1.00',
  PRIMARY KEY (`game_id`),
  CONSTRAINT `game_id_shapes_fk` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shapes`
--

LOCK TABLES `shapes` WRITE;
/*!40000 ALTER TABLE `shapes` DISABLE KEYS */;
INSERT INTO `shapes` VALUES (30,9.99,10.00,10.01,10.00,10.00,9.99,10.01,10.00,10.00,10.00,10.00,9.99,10.00,10.00),(31,9.99,10.00,10.00,10.01,9.99,10.00,10.00,10.00,10.00,10.00,10.00,10.00,10.00,10.00),(32,9.99,9.99,10.00,10.00,10.00,0.00,9.99,10.01,9.99,10.00,10.00,10.01,10.01,10.00),(34,9.95,9.95,9.95,9.96,9.96,9.95,9.96,9.95,9.96,9.96,9.96,9.96,9.96,9.95),(35,1.71,5.18,1.91,4.89,6.82,3.65,1.07,12.20,9.52,2.87,29.96,2.95,12.77,29.95),(36,44.95,44.95,44.95,44.95,44.96,44.96,44.96,44.96,44.95,44.96,44.95,44.95,44.96,44.96),(37,29.96,29.96,14.57,29.96,27.59,29.95,29.96,29.96,29.95,29.97,29.96,29.96,29.96,29.95),(38,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00),(39,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00),(41,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00),(42,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00),(43,0.96,0.00,0.96,0.96,0.96,0.97,0.97,0.95,0.97,0.97,0.95,0.95,1.00,0.96);
/*!40000 ALTER TABLE `shapes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `textures`
--

DROP TABLE IF EXISTS `textures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `textures` (
  `game_id` int(11) NOT NULL,
  `four_dots` decimal(5,2) DEFAULT '-1.00',
  `waves` decimal(5,2) DEFAULT '-1.00',
  `arrow_head` decimal(5,2) DEFAULT '-1.00',
  `strips` decimal(5,2) DEFAULT '-1.00',
  `happy_smiley` decimal(5,2) DEFAULT '-1.00',
  `spikes` decimal(5,2) DEFAULT '-1.00',
  `dollar` decimal(5,2) DEFAULT '-1.00',
  `net` decimal(5,2) DEFAULT '-1.00',
  `note` decimal(5,2) DEFAULT '-1.00',
  `arcs` decimal(5,2) DEFAULT '-1.00',
  `monitor` decimal(5,2) DEFAULT '-1.00',
  `sad_smiley` decimal(5,2) DEFAULT '-1.00',
  `strudel` decimal(5,2) DEFAULT '-1.00',
  `four_bubbles` decimal(5,2) DEFAULT '-1.00',
  `spiral` decimal(5,2) DEFAULT '-1.00',
  `squares` decimal(5,2) DEFAULT '-1.00',
  PRIMARY KEY (`game_id`),
  CONSTRAINT `game_id_texture_fk` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `textures`
--

LOCK TABLES `textures` WRITE;
/*!40000 ALTER TABLE `textures` DISABLE KEYS */;
INSERT INTO `textures` VALUES (31,10.00,10.01,10.00,10.01,10.01,10.00,9.99,10.00,9.99,10.00,10.00,10.00,9.99,10.00,9.99,10.00),(32,10.00,10.01,10.00,10.00,10.00,9.99,10.00,10.00,9.99,10.00,9.99,9.99,10.00,10.01,10.00,10.00),(33,10.00,10.00,9.99,10.00,10.00,10.00,10.00,9.99,10.00,10.00,10.00,10.01,10.01,0.00,9.99,10.01),(40,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00,-1.00);
/*!40000 ALTER TABLE `textures` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-28 17:58:37
