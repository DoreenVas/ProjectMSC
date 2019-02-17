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
  `num_recgonized_buttons` int(11) NOT NULL DEFAULT '0',
  `game_date` datetime DEFAULT NULL,
  `time_limit` int(11) NOT NULL DEFAULT '60',
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game`
--

LOCK TABLES `game` WRITE;
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
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
  `patient_age` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
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

-- Dump completed on 2019-02-17 14:29:42
