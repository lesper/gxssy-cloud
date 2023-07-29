/*
Navicat MySQL Data Transfer

Source Server         : tencent
Source Server Version : 50718
Source Host           : sh-cynosdbmysql-grp-rnpif1iq.sql.tencentcdb.com:28897
Source Database       : fox-cloud

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2023-07-30 02:04:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户 id',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `phone` varchar(64) NOT NULL DEFAULT '' COMMENT '电话号码',
  `province` varchar(64) NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(64) NOT NULL DEFAULT '' COMMENT '市',
  `address_detail` varchar(256) NOT NULL DEFAULT '' COMMENT '详细地址',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户地址表';

-- ----------------------------
-- Records of t_address
-- ----------------------------
INSERT INTO `t_address` VALUES ('10', '10', 'Admin', '18888888888', '湖北省', '武汉市', '洪山区光谷软件园', '2023-07-16 16:54:24', '2023-07-16 16:54:24');
INSERT INTO `t_address` VALUES ('11', '10', 'Qinyi', '166666666666', '湖北省', '武汉市', '洪山区光谷科技港', '2023-07-16 17:58:24', '2023-07-16 17:58:24');

-- ----------------------------
-- Table structure for t_balance
-- ----------------------------
DROP TABLE IF EXISTS `t_balance`;
CREATE TABLE `t_balance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户 id',
  `balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户余额',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_key` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户账户余额表';

-- ----------------------------
-- Records of t_balance
-- ----------------------------
INSERT INTO `t_balance` VALUES ('10', '10', '900000', '2023-07-16 17:17:30', '2023-07-28 04:09:12');

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `goods_category` varchar(64) NOT NULL DEFAULT '' COMMENT '商品类别',
  `brand_category` varchar(64) NOT NULL DEFAULT '' COMMENT '品牌分类',
  `goods_name` varchar(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_pic` varchar(256) NOT NULL DEFAULT '' COMMENT '商品图片',
  `goods_description` varchar(512) NOT NULL DEFAULT '' COMMENT '商品描述信息',
  `goods_status` int(11) NOT NULL DEFAULT '0' COMMENT '商品状态',
  `price` int(11) NOT NULL DEFAULT '0' COMMENT '商品价格',
  `supply` bigint(20) NOT NULL DEFAULT '0' COMMENT '总供应量',
  `inventory` bigint(20) NOT NULL DEFAULT '0' COMMENT '库存',
  `goods_property` varchar(1024) NOT NULL DEFAULT '' COMMENT '商品属性',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `goods_category_brand_name` (`goods_category`,`brand_category`,`goods_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of t_goods
-- ----------------------------
INSERT INTO `t_goods` VALUES ('10', '10001', '20001', 'iphone 11', '', '苹果手机', '101', '100000', '2000000', '1999690', '{\"color\":\"绿色\",\"material\":\"金属机身\",\"pattern\":\"纯色\",\"size\":\"12cm * 6.5cm\"}', '2023-07-18 14:56:16', '2023-07-28 04:09:11');
INSERT INTO `t_goods` VALUES ('11', '10001', '20001', 'iphone 12', '', '苹果手机', '101', '150000', '2000000', '2000000', '{\"color\":\"绿色\",\"material\":\"金属机身\",\"pattern\":\"纯色\",\"size\":\"12cm * 6.5cm\"}', '2023-07-18 14:56:16', '2023-07-18 14:56:16');
INSERT INTO `t_goods` VALUES ('12', '10001', '20001', 'iphone 13', '', '苹果手机', '101', '160000', '2000000', '1999866', '{\"color\":\"绿色\",\"material\":\"金属机身\",\"pattern\":\"纯色\",\"size\":\"12cm * 6.5cm\"}', '2023-07-18 14:56:16', '2023-07-28 03:32:36');

-- ----------------------------
-- Table structure for t_logistics
-- ----------------------------
DROP TABLE IF EXISTS `t_logistics`;
CREATE TABLE `t_logistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户 id',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单 id',
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户地址记录 id',
  `extra_info` varchar(512) NOT NULL COMMENT '备注信息(json 存储)',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='物流表';

-- ----------------------------
-- Records of t_logistics
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户 id',
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户地址记录 id',
  `order_detail` text NOT NULL COMMENT '订单详情(json 存储, goodsId, count)',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户订单表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('33', '10', '10', '[{\"count\":5,\"goodsId\":10}]', '2023-07-28 03:49:55', '2023-07-28 03:49:55');
INSERT INTO `t_order` VALUES ('35', '10', '10', '[{\"count\":5,\"goodsId\":10}]', '2023-07-28 04:09:09', '2023-07-28 04:09:09');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `username` varchar(64) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(256) NOT NULL DEFAULT '' COMMENT 'MD5 加密之后的密码',
  `extra_info` varchar(1024) NOT NULL DEFAULT '' COMMENT '额外的信息',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('10', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '{}', '2023-07-11 07:17:03', '2023-07-11 16:33:43');
INSERT INTO `t_user` VALUES ('11', 'fox-admin', 'e10adc3949ba59abbe56e057f20f883e', '{}', '2023-07-11 07:11:38', '2023-07-11 07:11:38');
INSERT INTO `t_user` VALUES ('12', 'guest', 'e10adc3949ba59abbe56e057f20f883e', '{}', '2023-07-11 16:37:32', '2023-07-11 16:37:32');
INSERT INTO `t_user` VALUES ('13', 'admin1', 'e10adc3949ba59abbe56e057f20f883e', '{}', '2023-07-14 16:40:04', '2023-07-14 16:40:04');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
INSERT INTO `undo_log` VALUES ('92', '428034115019739137', '192.168.1.106:8091:428034109252571136', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 03:34:49', '2023-07-28 03:34:49', null);
INSERT INTO `undo_log` VALUES ('97', '428035165080522753', '192.168.1.106:8091:428035153869148160', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 03:38:58', '2023-07-28 03:38:58', null);
INSERT INTO `undo_log` VALUES ('98', '428035159699230721', '192.168.1.106:8091:428035153869148160', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 03:38:59', '2023-07-28 03:38:59', null);
INSERT INTO `undo_log` VALUES ('103', '428035586704543745', '192.168.1.106:8091:428035580786380800', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 03:40:40', '2023-07-28 03:40:40', null);
INSERT INTO `undo_log` VALUES ('114', '428040835834056705', '192.168.1.106:8091:428040830188523520', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 04:01:31', '2023-07-28 04:01:31', null);
INSERT INTO `undo_log` VALUES ('123', '428043362377928705', '192.168.1.106:8091:428043360381440000', 'serializer=jackson', 0x7B7D, '1', '2023-07-28 04:11:32', '2023-07-28 04:11:32', null);
