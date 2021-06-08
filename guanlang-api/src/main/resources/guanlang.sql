/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : guanlang

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-03-31 15:59:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_chat_image`
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_image`;
CREATE TABLE `t_chat_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `to_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `has_read` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_chat_image
-- ----------------------------
INSERT INTO `t_chat_image` VALUES ('15', '', '', '580bfc719f734d328986f05423902514.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('16', '', '', 'fd0c8f4c93f84b79b4b41d82539ba15e.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('17', '', '', '8021dc1d0b68454cac3cfb52a3c394c6.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('18', '', '', '35b77e51a41d43778259d6e0dc9707bd.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('19', '', '', 'd0cce18b996542598c2a2cb58b1f7ba7.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('20', '', '', 'b97452187b2d4351a7f8091fc6e0c0c5.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('21', '', '', 'f656d4b4f7174938b730199ad17e183b.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('22', '', '', '5d0d785bb62343068de4f459903bade4.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('23', '', '', '06214d041ce748418d17b7ed8b0c5430.pdf', '0');
INSERT INTO `t_chat_image` VALUES ('24', '', '', '5e26d0559fc94c09a934a133c5ad4c1b.pdf', '0');

-- ----------------------------
-- Table structure for `t_permit`
-- ----------------------------
DROP TABLE IF EXISTS `t_permit`;
CREATE TABLE `t_permit` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` tinyint(4) DEFAULT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `path` varchar(200) NOT NULL,
  `parent_id` tinyint(10) NOT NULL,
  `type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_permit
-- ----------------------------
INSERT INTO `t_permit` VALUES ('1', '2020-03-27 16:56:54', '2020-03-27 16:56:54', '1', '机器人管理', '/glxjServer/machine/list', '0', null);
INSERT INTO `t_permit` VALUES ('2', '2020-03-03 10:22:50', '2020-03-03 10:22:50', '1', '告警列表', '/glxjServer/warning/list', '0', null);
INSERT INTO `t_permit` VALUES ('3', '2020-03-03 10:25:07', '2020-03-03 10:25:10', '1', '权限管理', '/glxjServer/system/perManage', '0', null);
INSERT INTO `t_permit` VALUES ('4', '2020-03-31 11:13:36', '2020-03-31 11:13:36', '1', '机器人列表', '/123123', '1', null);
INSERT INTO `t_permit` VALUES ('5', '2020-03-31 11:24:20', '2020-03-31 11:24:23', '1', '机器人列表1', '/123', '4', null);

-- ----------------------------
-- Table structure for `t_permit_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_permit_role`;
CREATE TABLE `t_permit_role` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` tinyint(4) NOT NULL,
  `permit_id` tinyint(4) NOT NULL,
  `role_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_permit_role
-- ----------------------------
INSERT INTO `t_permit_role` VALUES ('1', '2020-03-03 10:20:00', '2020-03-03 10:20:00', '1', '1', '1');
INSERT INTO `t_permit_role` VALUES ('2', '2020-03-03 10:21:46', '2020-03-03 10:21:48', '1', '2', '1');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `chinese_name` varchar(100) NOT NULL,
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` tinyint(4) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nick_name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('管理员', '1', '2020-03-26 09:45:25', '2020-03-26 09:45:25', '1', 'admin', '系统管理员');
INSERT INTO `t_role` VALUES ('运维', '2', '2020-03-26 09:45:28', '2020-03-26 09:45:28', '1', 'opt', '运维人员');
INSERT INTO `t_role` VALUES ('专家', '3', '2020-03-26 09:45:32', '2020-03-26 09:45:32', '1', 'expert', '专家');
INSERT INTO `t_role` VALUES ('普通', '4', '2020-03-26 09:45:38', '2020-03-26 09:45:38', '1', 'general', '普通用户');

-- ----------------------------
-- Table structure for `t_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_user`;
CREATE TABLE `t_role_user` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` tinyint(4) NOT NULL,
  `user_id` tinyint(4) NOT NULL,
  `role_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_role_user
-- ----------------------------
INSERT INTO `t_role_user` VALUES ('1', '2020-03-20 16:57:51', '2020-03-20 16:57:51', '1', '1', '1');
INSERT INTO `t_role_user` VALUES ('2', '2020-03-27 13:50:10', '2020-03-27 13:50:10', '1', '0', '1');
INSERT INTO `t_role_user` VALUES ('3', '2020-03-27 14:02:05', '2020-03-27 14:02:05', '1', '0', '1');
INSERT INTO `t_role_user` VALUES ('4', '2020-03-27 15:29:22', '2020-03-27 15:29:22', '1', '0', '1');

-- ----------------------------
-- Table structure for `t_system_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_system_user`;
CREATE TABLE `t_system_user` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `enable` tinyint(4) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tel_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `salt` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_system_user
-- ----------------------------
INSERT INTO `t_system_user` VALUES ('1', '2020-03-27 15:42:41', '2020-03-27 15:42:41', '1', '38b77d01a58ed2d9fe00fb5539732cbe', 'admin', '18116335073', 'd7d68d1dc150d8cc22cb082498a6ccc1');
INSERT INTO `t_system_user` VALUES ('30', '2020-03-27 14:02:05', '2020-03-27 14:02:05', '1', '0a6981eda389c002c3dfb918144bca7e', 'admin1', '18116335073', '9308174141eab91f1c21002bf8861dd3');
INSERT INTO `t_system_user` VALUES ('31', '2020-03-27 15:29:22', '2020-03-27 15:29:22', '1', 'e80b519b1661aed44ecf9b086d1cdf56', 'admin12', '18116335073', '57a9962ade1e8a0134cb966dbb0a30d5');

-- ----------------------------
-- Function structure for `getChildList`
-- ----------------------------
DROP FUNCTION IF EXISTS `getChildList`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getChildList`(rootId INT) RETURNS varchar(1000) CHARSET utf8mb4
BEGIN
	DECLARE
		cTemp,
		cTempChild VARCHAR (1000);


SET cTemp = '$';


SET cTempChild = CAST(rootId AS CHAR);


WHILE cTempChild IS NOT NULL DO

SET cTemp = CONCAT(cTemp, ',', cTempChild);

SELECT
	GROUP_CONCAT(id) INTO cTempChild
FROM
	t_permit
WHERE
	FIND_IN_SET(parent_id, cTempChild) > 0;


END
WHILE;

RETURN cTemp;


END
;;
DELIMITER ;
