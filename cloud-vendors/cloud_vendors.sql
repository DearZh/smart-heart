/*
 Navicat Premium Data Transfer

 Source Server         : 微软云40.73.82.1
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 40.73.82.1:3306
 Source Schema         : cloud_vendors

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 05/02/2021 15:06:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for build_osm
-- ----------------------------
DROP TABLE IF EXISTS `build_osm`;
CREATE TABLE `build_osm`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `status` int(11) NOT NULL COMMENT '工单状态',
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工单标题',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工单内容',
  `product_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品ID',
  `product_business_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '	产品问题类别ID',
  `custom_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '用户ID',
  `tenant_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '租户ID',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for build_osm_message
-- ----------------------------
DROP TABLE IF EXISTS `build_osm_message`;
CREATE TABLE `build_osm_message`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `case_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工单ID',
  `type` int(11) NOT NULL COMMENT '	留言方式',
  `replier_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '	留言内容',
  `content` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_osm_detail
-- ----------------------------
DROP TABLE IF EXISTS `cloud_osm_detail`;
CREATE TABLE `cloud_osm_detail`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `case_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工单ID(云厂商ID)',
  `message_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单状态（0表示以结单数据，1 表示非完整数据）',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单类型：ALIBABA，HUAWEI',
  `replier` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言者',
  `replier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言者名称',
  `content` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言内容',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_osm_list
-- ----------------------------
DROP TABLE IF EXISTS `cloud_osm_list`;
CREATE TABLE `cloud_osm_list`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `cloud_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '工单ID(云厂商ID)',
  `status` int(11) NULL DEFAULT NULL COMMENT '工单状态（0表示以结单数据，1 表示非完整数据）',
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单标题',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'email',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单类型：ALIBABA，HUAWEI',
  `product_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品ID',
  `product_business_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品问题类别ID',
  `osm_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单来源ID',
  `custom_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户ID （cloud_user_id）',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '云工单列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_osm_product
-- ----------------------------
DROP TABLE IF EXISTS `cloud_osm_product`;
CREATE TABLE `cloud_osm_product`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品ID（直接使用云产商所返回的产品ID）',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单类型：ALIBABA，HUAWEI',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品名称',
  `description` varchar(5000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品备注',
  `acronym` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品类型简称（华为专用）',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单产品信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_osm_product_type
-- ----------------------------
DROP TABLE IF EXISTS `cloud_osm_product_type`;
CREATE TABLE `cloud_osm_product_type`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品问题类别ID（直接使用云产商所返回的产品ID）',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单类型：ALIBABA，HUAWEI',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '产品问题类别名称',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单产品信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_user
-- ----------------------------
DROP TABLE IF EXISTS `cloud_user`;
CREATE TABLE `cloud_user`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `secret_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'secret_id',
  `secret_key_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'secret_key_secret',
  `region_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '地域',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'HUAWEI,ALIBABA,TENCENT',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '云厂商账户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for osm_problem_type
-- ----------------------------
DROP TABLE IF EXISTS `osm_problem_type`;
CREATE TABLE `osm_problem_type`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `create_time` mediumtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单问题类型' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
