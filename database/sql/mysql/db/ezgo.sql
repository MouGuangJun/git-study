-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ezgo
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- create database `ezgo`
--

CREATE DATABASE IF NOT EXISTS `ezgo`;
USE ezgo;

--
-- Table structure for table `ezgo_payment`
--

DROP TABLE IF EXISTS `ezgo_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ezgo_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `serial_no` varchar(48) DEFAULT NULL,
  `input_date` varchar(10) DEFAULT NULL,
  `update_date` varchar(10) DEFAULT NULL,
  `note` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ezgo_payment`
--

LOCK TABLES `ezgo_payment` WRITE;
/*!40000 ALTER TABLE `ezgo_payment` DISABLE KEYS */;
INSERT INTO `ezgo_payment` VALUES (1,'202207010001','2022/07/01','2022/07/01','first record'),(2,'1','2022/07/01','2022/07/01','update from spring datasource'),(3,'PI2022070300000001','2022/07/03','2022/07/03','from web'),(8,'PI2022070300000002','2022/07/03','2022/07/03','from order'),(9,'PI2022070300000002','2022/07/03','2022/07/03','from order'),(10,'PI2022070300000003','2022/07/03','2022/07/03','from order'),(11,'PI2022070300000003','2022/07/26','2022/07/26','from order');
/*!40000 ALTER TABLE `ezgo_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serial_record`
--

DROP TABLE IF EXISTS `serial_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `serial_record` (
  `table_name` varchar(48) NOT NULL COMMENT '表名',
  `column_name` varchar(48) NOT NULL COMMENT '字段名',
  `current_serial` varchar(48) DEFAULT NULL COMMENT '当前最大的流水号',
  PRIMARY KEY (`table_name`,`column_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serial_record`
--

LOCK TABLES `serial_record` WRITE;
/*!40000 ALTER TABLE `serial_record` DISABLE KEYS */;
INSERT INTO `serial_record` VALUES ('BUSINESS_APPLY','SERIALNO','2022072700000602'),('EZGO_PAYMENT','SERIAL','2022070300000177');
/*!40000 ALTER TABLE `serial_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-20 17:11:14
