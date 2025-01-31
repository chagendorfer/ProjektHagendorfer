CREATE DATABASE  IF NOT EXISTS `projektdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `projektdb`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: projektdb
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `befund`
--

DROP TABLE IF EXISTS `befund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `befund` (
  `BefundID` int NOT NULL AUTO_INCREMENT,
  `PatientID` int NOT NULL,
  `Pfad` text COLLATE utf8mb4_unicode_ci,
  `Datum` date DEFAULT NULL,
  PRIMARY KEY (`BefundID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `befund`
--

LOCK TABLES `befund` WRITE;
/*!40000 ALTER TABLE `befund` DISABLE KEYS */;
INSERT INTO `befund` VALUES (1,1,'/befunde/johann_muster_befund_2024_12_16.pdf','2024-12-16'),(2,2,'/befunde/anna_beispiel_befund_2024_12_16.pdf','2024-12-16');
/*!40000 ALTER TABLE `befund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bundesland`
--

DROP TABLE IF EXISTS `bundesland`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bundesland` (
  `BundeslandID` int NOT NULL AUTO_INCREMENT,
  `Bezeichnung` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`BundeslandID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bundesland`
--

LOCK TABLES `bundesland` WRITE;
/*!40000 ALTER TABLE `bundesland` DISABLE KEYS */;
INSERT INTO `bundesland` VALUES (1,'Burgenland'),(2,'Kärnten'),(3,'Niederösterreich'),(4,'Oberösterreich'),(5,'Salzburg'),(6,'Steiermark'),(7,'Tirol'),(8,'Vorarlberg'),(9,'Wien');
/*!40000 ALTER TABLE `bundesland` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `geschlecht`
--

DROP TABLE IF EXISTS `geschlecht`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `geschlecht` (
  `GeschlechtID` int NOT NULL AUTO_INCREMENT,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`GeschlechtID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `geschlecht`
--

LOCK TABLES `geschlecht` WRITE;
/*!40000 ALTER TABLE `geschlecht` DISABLE KEYS */;
INSERT INTO `geschlecht` VALUES (1,'Männlich'),(2,'Weiblich'),(3,'Divers');
/*!40000 ALTER TABLE `geschlecht` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `krankenkasse`
--

DROP TABLE IF EXISTS `krankenkasse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `krankenkasse` (
  `KrankenkasseID` int NOT NULL AUTO_INCREMENT,
  `Bezeichnung` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`KrankenkasseID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `krankenkasse`
--

LOCK TABLES `krankenkasse` WRITE;
/*!40000 ALTER TABLE `krankenkasse` DISABLE KEYS */;
INSERT INTO `krankenkasse` VALUES (1,'ÖGK - Österreichische Gesundheitskasse'),(2,'BVAEB - Versicherungsanstalt öffentlich Bediensteter, Eisenbahnen und Bergbau'),(3,'SVS - Sozialversicherungsanstalt der Selbständigen'),(4,'Private Krankenversicherung');
/*!40000 ALTER TABLE `krankenkasse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `PatientID` int NOT NULL AUTO_INCREMENT,
  `Vorname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Nachname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Anrede` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Geburtsdatum` date DEFAULT NULL,
  `Strasse` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `PLZ` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `Ort` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `BundeslandID` int DEFAULT NULL,
  `Telefon` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `GeschlechtID` int DEFAULT NULL,
  `KrankenkasseID` int DEFAULT NULL,
  `Sonstiges` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`PatientID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'Johann','Muster','Herr','1985-06-15','Hauptstraße 10','1010','Wien',9,'+436641234567',1,1,'Allergie gegen Pollen'),(2,'Anna','Beispiel','Frau','1990-03-22','Linzergasse 15','5020','Salzburg',5,'+436509876543',2,2,'Vegetarierin'),(3,'Johann','Muster','Herr','1985-06-15','Hauptstraße 10','1010','Wien',9,'+436641234567',1,1,'Allergie gegen Pollen'),(4,'Anna','Beispiel','Frau','1990-03-22','Linzergasse 15','5020','Salzburg',5,'+436509876543',2,2,'Vegetarierin'),(5,'Michael','Huber','Herr','1978-11-03','Schlossallee 3','4020','Linz',4,'+436601234567',1,3,'Diabetes Typ 2'),(6,'Sabine','Maier','Frau','1982-07-19','Ringstraße 12','8010','Graz',6,'+436761234567',2,4,'Laktoseintoleranz'),(7,'Klaus','Schmidt','Herr','1995-01-25','Bahnhofstraße 7','6020','Innsbruck',7,'+436771234567',1,1,'Raucher'),(8,'Petra','Wagner','Frau','1988-05-14','Seestraße 21','5700','Zell am See',5,'+436881234567',2,2,'Glutenunverträglichkeit'),(9,'Thomas','Gruber','Herr','1965-09-12','Landstraße 18','3100','St. Pölten',3,'+436991234567',1,1,'Bluthochdruck'),(10,'Elisabeth','Fischer','Frau','1993-12-08','Kirchgasse 9','7000','Eisenstadt',1,'+436781234567',2,4,'Asthma'),(11,'Markus','Leitner','Herr','1980-02-17','Dorfstraße 5','3500','Krems an der Donau',3,'+436551234567',1,3,'Vegetarier'),(12,'Claudia','Schneider','Frau','2000-04-23','Berggasse 16','5020','Salzburg',5,'+436641234890',2,1,'Keine besonderen Anmerkungen'),(13,'Alex','Kern','Mx.','1992-08-15','Gartenweg 22','8010','Graz',6,'+436601112223',3,1,'Allergie gegen Tierhaare');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'projektdb'
--

--
-- Dumping routines for database 'projektdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-31 15:56:37
