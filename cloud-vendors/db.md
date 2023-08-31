http://139.217.187.50:4999/web/#/p/0f393bbf42577d933bf6ee262bc24e93

```

# 云厂商账户信息

    

-  云厂商账户信息  cloud_user

|字段|类型|空|默认|注释|
|:----    |:-------    |:--- |-- -|------      |
|id    |varchar(10)     |否 |  |       id      |
|secret_id |varchar |否 |    |   用于标识 API 调用者身份 |
|secret_key_secret |varchar(500) |否 |    |   用于加密签名字符串和服务器端验证签名字符串的密钥  |
|region_id |varchar(500) |否 |    |   API地域信息  |
|type |varchar(50) |否   |    |  类型： tencent,huawei,alibaba    |
|create_time |long |否   |    |  创建时间戳    |


CREATE TABLE `cloud_user`  (
  `id` varchar(50) NOT NULL  COMMENT 'ID',
  `secret_id` varchar(50) NOT NULL COMMENT 'secret_id',
  `secret_key_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'secret_key_secret',
	`region_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '地域',
	`type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'HUAWEI,ALIBABA,TENCENT',
  `create_time` long CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
	) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '云厂商账户信息表' ROW_FORMAT = Dynamic;




- 备注：无

# 工单列表

-  工单列表


|字段|类型|空|默认|注释|
|:----    |:-------    |:--- |-- -|------|
|id    |varchar(10)     |否 |  |工单ID(自生成ID)|
|cloud_id    |varchar(10)     |否 |  |工单ID(云厂商ID)|
|status |varchar |否 |    |   工单状态（0表示以结单数据，1 表示非完整数据）  |
|name |varchar(500) |否 |    |   工单标题  |
|email |varchar(50) |否 |    |   email  |
|type |varchar(50) |否   |    |  工单类型：0阿里，1华为    |
|product_id    |varchar(10)     |否 |  |       产品ID   |
|product_business_id    |varchar(10)     |否 |  |       产品问题类别ID   |
|osm_id    |varchar(10)     |否 |  |       工单来源ID |
|custom_id    |varchar(10)     |否 |  |       用户ID   （cloud_user_id）|
|create_time |long |否   |    |   工单创建时间    |

- 备注：工单的列表数据：新建工单成功后，将对应的工单详情数据直接存储至工单表中，此时不做留言的数据存储，只存储该详情即可；此时该工单数据类型为1；


CREATE TABLE `cloud_osm_list`  (
  `id` varchar(50) NOT NULL  COMMENT 'ID',
  `cloud_id` varchar(50) NOT NULL COMMENT '工单ID(云厂商ID)',
  `status` int   COMMENT '工单状态（0表示以结单数据，1 表示非完整数据）',
    `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单标题',
	    `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT 'email',
      `type`  varchar(50)    COMMENT '工单类型：ALIBABA，HUAWEI',
		`product_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品ID',
    `product_business_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品问题类别ID',
    `osm_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单来源ID',
    `custom_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户ID （cloud_user_id）',
  `create_time` long CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '云工单列表' ROW_FORMAT = Dynamic;
		


# 工单记录


-  工单记录


|字段|类型|空|默认|注释|
|:----    |:-------    |:--- |-- -|------      |
|id   | varchar(10)     |否 |  |             |
|case_id   | varchar(10)     |否 |  |     工单ID        |
|message_id | varchar(20) |否 |    |   云厂商生成的留言ID  |
|type | varchar(20) |否 |    |   留言方式（0表示客户留言，1 客服回留言）  |
|replier |varchar(15) |是   |    |    留言者     |
|replier_name|varchar(15) |是   |    |    留言者名称     |
|content |varchar(15) |是   |    |    留言内容     |
|create_time | long |否   |    |   创建时间    |

- 备注：用户点击关闭工单后，根据工单ID，查询对应的工单聊天记录信息，并同步至该工单记录表中


CREATE TABLE `cloud_osm_detail`  (
  `id` varchar(50) NOT NULL  COMMENT 'ID',
  `case_id` varchar(50) NOT NULL COMMENT '工单ID(云厂商ID)',
  `message_id` varchar(50)    COMMENT '工单状态（0表示以结单数据，1 表示非完整数据）',
      `type`  varchar(50)    COMMENT '工单类型：ALIBABA，HUAWEI',
	`replier` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言者',
    `replier_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言者名称',
    `content` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '留言内容',
  `create_time` long CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单详情' ROW_FORMAT = Dynamic;


-----------


# 工单产品

-  工单产品


|字段|类型|空|默认|注释|
|:----    |:-------    |:--- |-- -|------      |
|id   | varchar(10)     |否 | 产品ID（直接使用云产商所返回的产品ID） |             |
|name | varchar(200) |否 |    |   产品名称|
|description | varchar(200) |否 |    |  产品备注 |
|acronym   | varchar(10)     |否 |  |     产品类型简称（华为专用）       |
|create_time | long |否   |    |   创建时间    |



- 备注：对外提供 refresh 接口，批量同步一次云工单产品&工单问题类别数据；（云平台这些数据的ID是不会经常变更的，同步一次，DB中获取到该数据即可；）

CREATE TABLE `cloud_osm_product`  (
  `id` varchar(50) NOT NULL  COMMENT '产品ID（直接使用云产商所返回的产品ID）',
      `type`  varchar(50)    COMMENT '工单类型：ALIBABA，HUAWEI',
  `name` varchar(50) NOT NULL COMMENT '产品名称',
  `description` varchar(5000)    COMMENT '产品备注',
		`acronym` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品类型简称（华为专用）',
  `create_time` long CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单产品信息' ROW_FORMAT = Dynamic;



# 工单产品问题类别

-  工单产品类型


|字段|类型|空|默认|注释|
|:----    |:-------    |:--- |-- -|------      |
|id   | varchar(10)     |否 | 产品问题类别ID（直接使用云产商所返回的产品ID） |             |
|name   | varchar(200)     |否 |  |     产品问题类别名称        |
|create_time | long |否   |    |   创建时间    |


- 备注：无

CREATE TABLE `cloud_osm_product_type`  (
  `id` varchar(50) NOT NULL  COMMENT '产品问题类别ID（直接使用云产商所返回的产品ID）',
   `type`  varchar(50)    COMMENT '工单类型：ALIBABA，HUAWEI',
  `name` varchar(50) NOT NULL COMMENT '产品问题类别名称',
  `create_time` long CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '工单创建时间',
  PRIMARY KEY (`id`) USING BTREE
    ) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci COMMENT = '工单产品信息' ROW_FORMAT = Dynamic;


# 工单来源（待华为新增该接口）

-  工单来源
```