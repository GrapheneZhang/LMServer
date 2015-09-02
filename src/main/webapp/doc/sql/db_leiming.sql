/*
Navicat MySQL Data Transfer

Source Server         : leiming
Source Server Version : 50537
Source Host           : 123.57.81.43:3306
Source Database       : db_leiming

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2015-09-02 16:14:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lm_question
-- ----------------------------
DROP TABLE IF EXISTS `lm_question`;
CREATE TABLE `lm_question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` varchar(5000) DEFAULT '' COMMENT '问题内容',
  `answer` varchar(5000) DEFAULT '' COMMENT '问题的答案，可能是答案也可能是答案选项',
  `subject` smallint(5) DEFAULT NULL COMMENT '题目科目',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_TOPIC_TOPIC_TYPE` (`subject`),
  CONSTRAINT `fk_lm_question_lm_subject_1` FOREIGN KEY (`subject`) REFERENCES `lm_subject` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lm_question
-- ----------------------------

-- ----------------------------
-- Table structure for lm_subject
-- ----------------------------
DROP TABLE IF EXISTS `lm_subject`;
CREATE TABLE `lm_subject` (
  `id` smallint(5) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `en_name` varchar(255) NOT NULL DEFAULT '' COMMENT '英文科目名',
  `zh_name` varchar(255) NOT NULL DEFAULT '' COMMENT '中文科目名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_NAME` (`en_name`,`zh_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lm_subject
-- ----------------------------

-- ----------------------------
-- Table structure for lm_user
-- ----------------------------
DROP TABLE IF EXISTS `lm_user`;
CREATE TABLE `lm_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(20) NOT NULL DEFAULT '' COMMENT '登录名',
  `user_mac` varchar(20) DEFAULT '' COMMENT '用户手机Mac地址',
  `user_proof_rule` varchar(80) DEFAULT '' COMMENT '用户令牌',
  `is_active` bit(1) DEFAULT b'0' COMMENT '是否激活，0位激活，1已激活',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_NAME_MAC_PROOF` (`user_name`,`user_mac`,`user_proof_rule`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lm_user
-- ----------------------------
INSERT INTO `lm_user` VALUES ('1', '18515012319', 'sdfasd', 'sdfsdf', '\0');
INSERT INTO `lm_user` VALUES ('2', '18515012320', 'ssdf11', 'sdfsdfs11', '');

-- ----------------------------
-- Table structure for lm_user_subject
-- ----------------------------
DROP TABLE IF EXISTS `lm_user_subject`;
CREATE TABLE `lm_user_subject` (
  `user_id` bigint(20) NOT NULL COMMENT '主 外键依赖user',
  `subject_id` smallint(6) NOT NULL COMMENT '主 外键依赖subject',
  PRIMARY KEY (`user_id`,`subject_id`),
  KEY `fk_lm_user_subject_lm_subject_1` (`subject_id`),
  CONSTRAINT `fk_lm_user_subject_lm_subject_1` FOREIGN KEY (`subject_id`) REFERENCES `lm_subject` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_lm_user_subject_lm_user_1` FOREIGN KEY (`user_id`) REFERENCES `lm_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lm_user_subject
-- ----------------------------

-- ----------------------------
-- Table structure for sys_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_privilege`;
CREATE TABLE `sys_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '权限名',
  `action_url` varchar(500) DEFAULT '' COMMENT '权限url',
  `parent_id` int(11) DEFAULT NULL COMMENT '父权限，依赖于id字段',
  `is_menu` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是菜单，1是，0否',
  `icon` varchar(500) DEFAULT '' COMMENT '权限的图标',
  `description` varchar(100) DEFAULT '' COMMENT '权限描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_PRIVILEGE_PARENTID_ID` (`parent_id`),
  CONSTRAINT `FK_PRIVILEGE_PARENTID_ID` FOREIGN KEY (`parent_id`) REFERENCES `sys_privilege` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_privilege
-- ----------------------------
INSERT INTO `sys_privilege` VALUES ('1', '系统管理', '', null, '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '管理权限分配的权限', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('2', '用户管理', '/lm/user/query', '1', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '用户列表', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('3', '角色管理', '/lm/role/query', '1', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '角色列表', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('4', '权限管理', '/lm/privilege/query', '1', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '权限列表', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('5', '用户_新增', '/lm/user/add', '2', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '用户_新增', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('6', '用户_删除', '/lm/user/delete', '2', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '用户_删除', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('7', '用户_修改', '/lm/user/update', '2', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '用户_修改', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('8', '角色_新增', '/lm/role/add', '3', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '角色_新增', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('9', '角色_删除', '/lm/role/delete', '3', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '角色_删除', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('10', '角色_修改', '/lm/role/update', '3', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '角色_修改', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('11', '权限_新增', '/lm/privilege/add', '4', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '权限_新增', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('12', '权限_删除', '/lm/privilege/delete', '4', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '权限_删除', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('13', '权限_修改', '/lm/privilege/update', '4', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '权限_修改', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_privilege` VALUES ('14', '雷鸣管理', '', null, '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '雷鸣管理', '2015-09-01 09:59:05', '2015-09-01 10:07:22');
INSERT INTO `sys_privilege` VALUES ('15', '雷鸣用户管理', '/lm/lmuser/query', '14', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '', '2015-09-01 10:07:15', '2015-09-01 10:07:15');
INSERT INTO `sys_privilege` VALUES ('16', '科目管理', '/lm/subject/query', '14', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '', '2015-09-02 13:59:42', '2015-09-02 14:00:56');
INSERT INTO `sys_privilege` VALUES ('17', '题目管理', '/lm/question/query', '14', '', '/lm/js/widget/zTree3.5/img/diy/1_close.png', '', '2015-09-02 14:00:39', '2015-09-02 14:00:39');
INSERT INTO `sys_privilege` VALUES ('18', '科目_新增', '/lm/subject/add', '16', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:02:00', '2015-09-02 14:02:00');
INSERT INTO `sys_privilege` VALUES ('19', '科目_修改', '/lm/subject/update', '16', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:02:31', '2015-09-02 14:02:31');
INSERT INTO `sys_privilege` VALUES ('20', '科目_删除', '/lm/subject/delete', '16', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:03:00', '2015-09-02 14:03:00');
INSERT INTO `sys_privilege` VALUES ('21', '雷鸣用户_新增', '/lm/lmuser/add', '15', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:05:47', '2015-09-02 14:05:47');
INSERT INTO `sys_privilege` VALUES ('22', '雷鸣用户_修改', '/lm/lmuser/update', '15', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:06:12', '2015-09-02 14:06:12');
INSERT INTO `sys_privilege` VALUES ('23', '雷鸣用户_删除', '/lm/lmuser/delete', '15', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:06:35', '2015-09-02 14:06:35');
INSERT INTO `sys_privilege` VALUES ('24', '题目_新增', '/lm/question/add', '17', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:07:31', '2015-09-02 14:07:31');
INSERT INTO `sys_privilege` VALUES ('25', '题目_修改', '/lm/question/update', '17', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:08:17', '2015-09-02 14:08:17');
INSERT INTO `sys_privilege` VALUES ('26', '题目_删除', '/lm/question/delete', '17', '\0', '/lm/js/widget/zTree3.5/img/diy/3.png', '', '2015-09-02 14:08:46', '2015-09-02 14:08:46');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  `description` varchar(100) DEFAULT '' COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '这是有最高权限的超级管理员', '2015-08-20 00:01:01', '2015-08-20 00:01:01');
INSERT INTO `sys_role` VALUES ('2', '雷鸣管理员', '', '2015-09-01 09:59:30', '2015-09-02 14:09:03');

-- ----------------------------
-- Table structure for sys_role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_privilege`;
CREATE TABLE `sys_role_privilege` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `privilege_id` int(11) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`,`privilege_id`),
  KEY `fk_RP_PRIVILEGE` (`privilege_id`),
  CONSTRAINT `fk_RP_ROLE` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_RP_PRIVILEGE` FOREIGN KEY (`privilege_id`) REFERENCES `sys_privilege` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_privilege
-- ----------------------------
INSERT INTO `sys_role_privilege` VALUES ('1', '1');
INSERT INTO `sys_role_privilege` VALUES ('1', '2');
INSERT INTO `sys_role_privilege` VALUES ('1', '3');
INSERT INTO `sys_role_privilege` VALUES ('1', '4');
INSERT INTO `sys_role_privilege` VALUES ('1', '5');
INSERT INTO `sys_role_privilege` VALUES ('1', '6');
INSERT INTO `sys_role_privilege` VALUES ('1', '7');
INSERT INTO `sys_role_privilege` VALUES ('1', '8');
INSERT INTO `sys_role_privilege` VALUES ('1', '9');
INSERT INTO `sys_role_privilege` VALUES ('1', '10');
INSERT INTO `sys_role_privilege` VALUES ('1', '11');
INSERT INTO `sys_role_privilege` VALUES ('1', '12');
INSERT INTO `sys_role_privilege` VALUES ('1', '13');
INSERT INTO `sys_role_privilege` VALUES ('2', '14');
INSERT INTO `sys_role_privilege` VALUES ('2', '15');
INSERT INTO `sys_role_privilege` VALUES ('2', '16');
INSERT INTO `sys_role_privilege` VALUES ('2', '17');
INSERT INTO `sys_role_privilege` VALUES ('2', '18');
INSERT INTO `sys_role_privilege` VALUES ('2', '19');
INSERT INTO `sys_role_privilege` VALUES ('2', '20');
INSERT INTO `sys_role_privilege` VALUES ('2', '21');
INSERT INTO `sys_role_privilege` VALUES ('2', '22');
INSERT INTO `sys_role_privilege` VALUES ('2', '23');
INSERT INTO `sys_role_privilege` VALUES ('2', '24');
INSERT INTO `sys_role_privilege` VALUES ('2', '25');
INSERT INTO `sys_role_privilege` VALUES ('2', '26');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名，登陆用',
  `real_name` varchar(50) DEFAULT '' COMMENT '真实名字',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `mobile_no` char(11) DEFAULT '',
  `email` varchar(100) DEFAULT '',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `login_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '登陆次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '张三', 'admin', '13113112312', '12345@qq.com', '2015-08-20 00:01:01', '2015-09-02 14:38:26', '1');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_ROLE` (`role_id`),
  CONSTRAINT `fk_USER` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ROLE` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('1', '2');
