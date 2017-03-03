/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : mw_web_app

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-03-01 17:22:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for it_lib_article
-- ----------------------------
DROP TABLE IF EXISTS `it_lib_article`;
CREATE TABLE `it_lib_article` (
  `id` varchar(36) NOT NULL,
  `lib_id` varchar(36) NOT NULL,
  `article_id` varchar(36) NOT NULL,
  `create_on` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of it_lib_article
-- ----------------------------
