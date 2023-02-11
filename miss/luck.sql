/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50739
Source Host           : localhost:3306
Source Database       : luck

Target Server Type    : MYSQL
Target Server Version : 50739
File Encoding         : 65001

Date: 2023-02-11 21:21:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_auth
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth`;
CREATE TABLE `sys_auth` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `auth_name` varchar(32) DEFAULT NULL,
  `auth_describe` varchar(32) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `url` varchar(200) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_auth
-- ----------------------------
INSERT INTO `sys_auth` VALUES ('1', '0', '账户信息', '父节点', null, null, '账户信息', null);
INSERT INTO `sys_auth` VALUES ('2', '1', '用户管理', '用户管理', '2023-02-05 11:24:18', '/home/users/list', '用户管理', '2023-02-05 11:24:18');
INSERT INTO `sys_auth` VALUES ('3', '1', '角色管理', '角色管理', '2023-02-05 16:19:39', '/home/role/list', '角色管理', '2023-02-05 16:19:39');
INSERT INTO `sys_auth` VALUES ('4', '1', '权限管理', '权限管理', '2023-02-05 11:34:27', '/home/menu/list', '权限管理', '2023-02-05 11:34:27');
INSERT INTO `sys_auth` VALUES ('5', '0', '企业信息', '父节点', '2023-02-10 19:37:57', null, '企业信息', '2023-02-10 19:37:57');
INSERT INTO `sys_auth` VALUES ('6', '5', '职位管理', '职位管理', '2023-02-10 21:16:49', '/home/role/list', '职位管理', '2023-02-10 21:16:49');
INSERT INTO `sys_auth` VALUES ('7', '0', '测试权限1', '测试============', '2023-02-10 21:15:41', '', '测试权限1', '2023-02-10 21:15:41');
INSERT INTO `sys_auth` VALUES ('11', '7', '测试菜单a', '测试菜单a', '2023-02-11 05:15:58', '/home/users/list', '测试菜单a', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) DEFAULT NULL,
  `role_describe` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `role_code` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级角色', '拥有一切权利的角色', '2023-02-06 14:41:27', '2023-02-06 14:41:27', 'super');
INSERT INTO `sys_role` VALUES ('2', '角色2', '角色2', '2023-02-05 09:08:27', null, null);
INSERT INTO `sys_role` VALUES ('3', '角色3', '角色3 ', '2023-02-05 09:08:41', null, null);
INSERT INTO `sys_role` VALUES ('5', '角色4', '角色4', null, null, null);
INSERT INTO `sys_role` VALUES ('6', '角5色5', '角色5角色5角色5角色5', '2023-02-10 12:22:07', '2023-02-10 20:22:08', null);
INSERT INTO `sys_role` VALUES ('7', '角色6', '角色6', null, null, null);
INSERT INTO `sys_role` VALUES ('8', '角色6', '角色6角色6角色6角色6', '2023-02-10 20:18:29', null, 'six');

-- ----------------------------
-- Table structure for sys_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_auth`;
CREATE TABLE `sys_role_auth` (
  `role_auth_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `auth_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_auth_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_auth
-- ----------------------------
INSERT INTO `sys_role_auth` VALUES ('1', '1', '2', '2023-02-05 09:31:20');
INSERT INTO `sys_role_auth` VALUES ('5', '1', '3', null);
INSERT INTO `sys_role_auth` VALUES ('6', '1', '4', null);
INSERT INTO `sys_role_auth` VALUES ('7', '1', '6', null);
INSERT INTO `sys_role_auth` VALUES ('8', '2', '3', '2023-02-06 14:28:47');
INSERT INTO `sys_role_auth` VALUES ('9', '3', '4', '2023-02-06 14:28:50');
INSERT INTO `sys_role_auth` VALUES ('10', '5', '3', '2023-02-06 22:29:00');
INSERT INTO `sys_role_auth` VALUES ('11', '5', '4', '2023-02-06 22:29:01');
INSERT INTO `sys_role_auth` VALUES ('27', '1', '11', '2023-02-11 05:15:58');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `real_name` varchar(32) DEFAULT NULL,
  `phone` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '123456', 'admin', '123456', '2023-02-11 12:31:10', '2023-02-11 20:31:11');
INSERT INTO `sys_user` VALUES ('2', 'a', '123456', 'a', '123456', '2023-02-04 23:03:14', '2023-02-05 22:14:34');
INSERT INTO `sys_user` VALUES ('3', 'bbbb', '1', 'b', '123456', '2023-02-06 14:16:58', '2023-02-06 22:16:59');
INSERT INTO `sys_user` VALUES ('4', 'c', '123456', 'c', '123456', '2023-02-04 23:03:14', '2023-02-05 22:14:35');
INSERT INTO `sys_user` VALUES ('6', 'test', '123456', '测试人', null, '2023-02-06 06:52:28', null);
INSERT INTO `sys_user` VALUES ('7', 'test2', '123456', '测试2', null, '2023-02-06 06:54:01', null);
INSERT INTO `sys_user` VALUES ('8', 'xiaomei', '123456', '小美', null, '2023-02-06 06:54:48', null);
INSERT INTO `sys_user` VALUES ('9', 'lan', '123456', '兰花', null, '2023-02-06 06:55:26', null);
INSERT INTO `sys_user` VALUES ('11', 'huan2', '123456', '还', null, '2023-02-12 03:05:43', null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1', '2023-02-05 09:09:22');
INSERT INTO `sys_user_role` VALUES ('3', '1', '3', '2023-02-05 09:09:26');
INSERT INTO `sys_user_role` VALUES ('8', '6', '6', '2023-02-07 05:50:22');
INSERT INTO `sys_user_role` VALUES ('9', '6', '5', '2023-02-07 05:50:22');
INSERT INTO `sys_user_role` VALUES ('10', '6', '3', '2023-02-07 05:50:22');
INSERT INTO `sys_user_role` VALUES ('11', '10', '5', null);
INSERT INTO `sys_user_role` VALUES ('12', '10', '6', null);
INSERT INTO `sys_user_role` VALUES ('17', '3', '7', '2023-02-11 18:32:58');
INSERT INTO `sys_user_role` VALUES ('18', '3', '5', '2023-02-11 18:32:58');
INSERT INTO `sys_user_role` VALUES ('21', '2', '3', '2023-02-11 18:38:09');
INSERT INTO `sys_user_role` VALUES ('22', '2', '8', '2023-02-11 18:38:09');
INSERT INTO `sys_user_role` VALUES ('23', '2', '6', '2023-02-11 18:38:09');
