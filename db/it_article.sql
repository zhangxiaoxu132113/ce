/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : mw_web_app

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-02-27 18:22:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for it_article
-- ----------------------------
DROP TABLE IF EXISTS `it_article`;
CREATE TABLE `it_article` (
  `id` varchar(36) NOT NULL,
  `title` varchar(250) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `author` varchar(1000) DEFAULT NULL,
  `category` varchar(36) DEFAULT NULL,
  `content` longtext,
  `reference` varchar(3000) DEFAULT NULL,
  `descrypt_url` varchar(2000) DEFAULT NULL,
  `release_time` varchar(100) DEFAULT NULL,
  `create_on` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
