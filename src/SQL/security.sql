git -- --------------------------------------------------------
-- Hôte:                         127.0.0.1
-- Version du serveur:           8.4.3 - MySQL Community Server - GPL
-- SE du serveur:                Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Listage de la structure de table security. diaries
CREATE TABLE IF NOT EXISTS `diaries` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `type` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `created_date` datetime NOT NULL,
  `finished_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_diaries_created_date` (`created_date`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table security.diaries : ~3 rows (environ)
INSERT INTO `diaries` (`id`, `title`, `type`, `content`, `created_date`, `finished_date`) VALUES
	(1, 'cassage camera', 'Accident', 'Camera cassée', '2026-03-31 00:00:00', '2026-03-31 00:00:00'),
	(2, 's', 'Incendie', 's ', '2026-04-01 00:00:00', '2026-04-28 00:00:00'),
	(3, 'd', 'Levé de doute', 'd', '2026-04-02 13:05:00', '2026-04-02 13:30:00');

-- Listage de la structure de table security. users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Listage des données de la table security.users : ~0 rows (environ)
INSERT INTO `users` (`id`, `username`, `first_name`, `last_name`, `pwd`, `is_admin`, `created_at`) VALUES
	(1, 'MattP', 'Matthieu', 'Perry', '$2a$12$VXy1/H1QoHDPREXSd0N4bOKbe6eArjw/TPMJ/zL8EXJmYnFWOym5G', 1, '2026-04-01 06:37:56');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
