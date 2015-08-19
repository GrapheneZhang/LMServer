/*
Navicat MySQL Data Transfer

Source Server         : localhost1
Source Server Version : 50168
Source Host           : localhost:3306
Source Database       : casual

Target Server Type    : MYSQL
Target Server Version : 50168
File Encoding         : 65001

Date: 2015-08-17 15:12:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_privilege`;
CREATE TABLE `sys_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '权限描述',
  `url` varchar(500) DEFAULT NULL COMMENT '权限url',
  `parent_id` int(11) DEFAULT NULL COMMENT '父权限',
  `icon` varchar(500) DEFAULT NULL COMMENT '权限的图标',
  `description` varchar(100) DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_privilege
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  `parent_id` int(11) DEFAULT NULL COMMENT '父角色',
  `description` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_privilege`;
CREATE TABLE `sys_role_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `privilege_id` int(11) DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  KEY `fk_RP_ROLE` (`role_id`),
  KEY `fk_RP_PRIVILEGE` (`privilege_id`),
  CONSTRAINT `fk_RP_PRIVILEGE` FOREIGN KEY (`privilege_id`) REFERENCES `sys_privilege` (`id`),
  CONSTRAINT `fk_RP_ROLE` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_privilege
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名，登陆用',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实名字',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `mobile_no` char(11) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `login_count` bigint(20) NOT NULL COMMENT '登陆次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `fk_USER` (`user_id`),
  KEY `fk_ROLE` (`role_id`),
  CONSTRAINT `fk_ROLE` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `fk_USER` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------