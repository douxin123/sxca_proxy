/*
Navicat MySQL Data Transfer

Source Server         : dev
Source Server Version : 50610
Source Host           : 192.168.153.23:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2018-11-06 09:50:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ex_message
-- ----------------------------
DROP TABLE IF EXISTS `ex_message`;
CREATE TABLE `ex_message` (
  `id` bigint(20) NOT NULL,
  `msg_data` varchar(1000) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0初始化 1处理中 2处理成功 3处理失败',
  `exception` text NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `accept_time` datetime NOT NULL COMMENT '接收时间',
  `finish_time` datetime DEFAULT NULL COMMENT '处理完时间',
  `retry` tinyint(2) NOT NULL COMMENT '重试次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ex_relation
-- ----------------------------
DROP TABLE IF EXISTS `ex_relation`;
CREATE TABLE `ex_relation` (
  `id` bigint(20) NOT NULL,
  `trade_id` varchar(32) NOT NULL,
  `data_id` bigint(20) NOT NULL,
  `data` mediumtext NOT NULL,
  `gmt_create` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT '数据类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `r_id` (`trade_id`,`data_id`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
