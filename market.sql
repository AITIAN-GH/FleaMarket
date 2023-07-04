
- ----------------------------
-- Table structure for sys_carousel
-- ----------------------------
DROP TABLE IF EXISTS `sys_carousel`;
CREATE TABLE `sys_carousel`  (
  `carousel_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `imgPath` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`carousel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_carousel
-- ----------------------------
INSERT INTO `sys_carousel` VALUES (1, 'carousel01', '/image/bindex01.jpg');
INSERT INTO `sys_carousel` VALUES (2, 'carousel01', '/image/bindex02.jpg');
INSERT INTO `sys_carousel` VALUES (3, 'carousel03', '/image/bindex03.jpg');
INSERT INTO `sys_carousel` VALUES (4, 'carousel04', '/image/bindex04.jpg');
INSERT INTO `sys_carousel` VALUES (5, 'carousel05', '/image/bindex05.jpg');
INSERT INTO `sys_carousel` VALUES (6, 'carousel06', '/image/bindex06.jpg');
INSERT INTO `sys_carousel` VALUES (7, 'carousel07', '/image/bindex07.jpg');
INSERT INTO `sys_carousel` VALUES (8, 'carousel08', '/image/bindex08.jpg');
INSERT INTO `sys_carousel` VALUES (9, 'carousel09', '/image/bindex09.jpg');
INSERT INTO `sys_carousel` VALUES (10, 'carousel10', '/image/bindex10.jpg');
INSERT INTO `sys_carousel` VALUES (11, 'carousel11', '/image/bindex11.jpg');
INSERT INTO `sys_carousel` VALUES (12, 'carousel12', '/image/bindex12.jpg');

-- ----------------------------
-- Table structure for sys_cart
-- ----------------------------
DROP TABLE IF EXISTS `sys_cart`;
CREATE TABLE `sys_cart`  (
  `cart_id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) NOT NULL,
  `product_id` int(10) NOT NULL,
  `count` int(20) NOT NULL DEFAULT 1,
  PRIMARY KEY (`cart_id`) USING BTREE,
  INDEX `bookfre`(`product_id` ASC) USING BTREE,
  INDEX `userfre`(`user_id` ASC) USING BTREE,
  CONSTRAINT `f_pro_id` FOREIGN KEY (`product_id`) REFERENCES `sys_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `f_u_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_cart
-- ----------------------------
INSERT INTO `sys_cart` VALUES (37, 8, 2, 1);

-- ----------------------------
-- Table structure for sys_classify
-- ----------------------------
DROP TABLE IF EXISTS `sys_classify`;
CREATE TABLE `sys_classify`  (
  `bid` int(10) NOT NULL AUTO_INCREMENT,
  `classify` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`bid`) USING BTREE,
  UNIQUE INDEX `bid`(`bid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_classify
-- ----------------------------
INSERT INTO `sys_classify` VALUES (1, '社会', '/shehui');
INSERT INTO `sys_classify` VALUES (2, '历史', '/history');
INSERT INTO `sys_classify` VALUES (3, '党史', '/dangshi');
INSERT INTO `sys_classify` VALUES (4, '玄幻', '/xuanhaun');
INSERT INTO `sys_classify` VALUES (5, '文学', '/wenxue');
INSERT INTO `sys_classify` VALUES (6, '专业', '/zhuanye');
INSERT INTO `sys_classify` VALUES (7, '科幻', '/kehuan');

-- ----------------------------
-- Table structure for sys_collection
-- ----------------------------
DROP TABLE IF EXISTS `sys_collection`;
CREATE TABLE `sys_collection`  (
  `user_id` int(12) NOT NULL COMMENT '用户id',
  `product_id` int(20) NOT NULL COMMENT '产品id',
  PRIMARY KEY (`user_id`, `product_id`) USING BTREE,
  INDEX `index_uid`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_collection
-- ----------------------------
INSERT INTO `sys_collection` VALUES (1, 1);
INSERT INTO `sys_collection` VALUES (1, 6);
INSERT INTO `sys_collection` VALUES (4, 6);
INSERT INTO `sys_collection` VALUES (4, 7);
INSERT INTO `sys_collection` VALUES (8, 4);
INSERT INTO `sys_collection` VALUES (8, 5);
INSERT INTO `sys_collection` VALUES (11, 3);
INSERT INTO `sys_collection` VALUES (11, 4);

-- ----------------------------
-- Table structure for sys_comment
-- ----------------------------
DROP TABLE IF EXISTS `sys_comment`;
CREATE TABLE `sys_comment`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论信息',
  `time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  `user_id` int(10) NOT NULL COMMENT '评论者ID',
  `product_id` int(10) NOT NULL COMMENT '产品ID',
  `pid` int(10) NULL DEFAULT 0 COMMENT '父评论ID',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fer_pro_id`(`product_id` ASC) USING BTREE,
  INDEX `fer_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fer_pro_id` FOREIGN KEY (`product_id`) REFERENCES `sys_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fer_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_comment
-- ----------------------------
INSERT INTO `sys_comment` VALUES (1, 'hahah', '2023-04-26 23:55:21', 1, 1, 0, 0);
INSERT INTO `sys_comment` VALUES (2, '哈哈哈 ', '2023-04-26 23:55:19', 2, 1, 1, 0);
INSERT INTO `sys_comment` VALUES (3, '就这？？', '2023-05-09 18:15:17', 2, 2, 0, 0);
INSERT INTO `sys_comment` VALUES (4, '也不行呀', '2023-05-09 18:15:20', 3, 3, 0, 0);
INSERT INTO `sys_comment` VALUES (5, '??????', '2023-04-26 23:55:17', 1, 1, 2, 0);
INSERT INTO `sys_comment` VALUES (8, '懂？', '2023-04-19 21:18:15', 1, 1, 5, 0);
INSERT INTO `sys_comment` VALUES (9, '啊哈哈啊哈哈哈哈', '2023-04-19 21:25:21', 1, 1, 0, 0);
INSERT INTO `sys_comment` VALUES (10, '你笑个屁呀', '2023-04-19 21:55:25', 4, 1, 9, 0);
INSERT INTO `sys_comment` VALUES (11, '嘎嘎好看啊！！', '2023-04-24 16:15:39', 4, 1, 0, 0);
INSERT INTO `sys_comment` VALUES (12, '老香了 干就完了！！', '2023-04-24 16:16:07', 4, 1, 0, 0);
INSERT INTO `sys_comment` VALUES (18, '啊哈？？', '2023-05-03 17:28:23', 8, 2, 3, 0);
INSERT INTO `sys_comment` VALUES (19, '家人们谁懂啊？？', '2023-05-03 17:36:36', 8, 2, 0, 0);
INSERT INTO `sys_comment` VALUES (20, '懂什么？？', '2023-05-03 17:43:00', 1, 2, 19, 0);
INSERT INTO `sys_comment` VALUES (21, '？？？？？', '2023-05-03 17:55:08', 1, 2, 0, 0);
INSERT INTO `sys_comment` VALUES (22, '你在疑惑些什么', '2023-05-03 18:32:44', 8, 2, 21, 0);
INSERT INTO `sys_comment` VALUES (23, '要你管', '2023-05-03 18:33:55', 1, 2, 22, 0);
INSERT INTO `sys_comment` VALUES (24, 'AAAAA', '2023-05-04 13:34:51', 2, 4, 23, 0);
INSERT INTO `sys_comment` VALUES (25, 'BBBBBB', '2023-05-04 13:35:48', 3, 4, 23, 0);
INSERT INTO `sys_comment` VALUES (26, 'CCCCC', '2023-05-04 13:36:28', 4, 2, 23, 0);
INSERT INTO `sys_comment` VALUES (27, '你管我呢', '2023-05-04 14:31:24', 1, 2, 22, 0);
INSERT INTO `sys_comment` VALUES (28, 'WS!', '2023-05-12 21:34:29', 1, 7, 0, 0);
INSERT INTO `sys_comment` VALUES (29, 'WS!!', '2023-05-12 21:36:19', 8, 7, 28, 0);
INSERT INTO `sys_comment` VALUES (30, 'WS!!!!!', '2023-05-12 21:49:10', 1, 7, 29, 0);
INSERT INTO `sys_comment` VALUES (31, 'CCCCCCCCC', '2023-05-12 23:58:27', 8, 7, 28, 0);
INSERT INTO `sys_comment` VALUES (32, 'AAAAA', '2023-05-12 23:58:56', 8, 7, 28, 0);
INSERT INTO `sys_comment` VALUES (33, '什么意思？？', '2023-05-13 14:33:05', 8, 7, 30, 0);
INSERT INTO `sys_comment` VALUES (34, '就哪个意思', '2023-05-13 14:33:19', 1, 7, 33, 0);
INSERT INTO `sys_comment` VALUES (35, '啊哈？', '2023-05-13 14:33:44', 8, 7, 34, 0);
INSERT INTO `sys_comment` VALUES (36, '呐喊评论测试1', '2023-06-05 18:11:06', 1, 5, 0, 0);
INSERT INTO `sys_comment` VALUES (37, '呐喊回复1', '2023-06-05 18:13:46', 8, 5, 36, 0);
INSERT INTO `sys_comment` VALUES (38, '哦哦好的', '2023-06-06 09:10:02', 8, 2, 27, 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '编码',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `c_d`(`code` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'House', 'house', '房子', 0);
INSERT INTO `sys_dict` VALUES (2, 'Star', 'star', '星星', 0);
INSERT INTO `sys_dict` VALUES (3, 'Minus', 'minus', '减小', 0);
INSERT INTO `sys_dict` VALUES (4, 'User', 'user', '用户', 0);
INSERT INTO `sys_dict` VALUES (5, 'Check', 'check', '对勾', 0);
INSERT INTO `sys_dict` VALUES (6, 'CloseBold', 'closeBold', '关闭', 0);
INSERT INTO `sys_dict` VALUES (7, 'Select', 'select', '对勾', 0);
INSERT INTO `sys_dict` VALUES (8, 'SemiSelect', 'semi select', '横杠', 0);
INSERT INTO `sys_dict` VALUES (9, 'Menu', 'menu', '夫级菜单', 0);
INSERT INTO `sys_dict` VALUES (11, 'Setting', 'setting', '设置', 0);
INSERT INTO `sys_dict` VALUES (12, 'EditPen', 'editPen', '编辑', 0);
INSERT INTO `sys_dict` VALUES (13, 'Grid', 'grid', '网格', 0);
INSERT INTO `sys_dict` VALUES (14, 'Reading', 'reading', '字典', 0);
INSERT INTO `sys_dict` VALUES (15, 'SwitchButton', 'SwitchButton', '电源', 0);
INSERT INTO `sys_dict` VALUES (16, 'Promotion', 'promotion', '小飞机', 0);
INSERT INTO `sys_dict` VALUES (17, 'Files', 'files', '文件列表', 0);
INSERT INTO `sys_dict` VALUES (18, 'ChatLineRound', 'ChatLineRound', '消息', 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `last_time` datetime NOT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1, '管理员 1', '127.0.0.1', '2023-06-06 13:25:28', '管理页登录, 登录状态：Login Success');
INSERT INTO `sys_log` VALUES (2, 'aitian01', '127.0.0.1', '2023-06-06 13:19:58', '管理页登录, 登录状态：Login Success');
INSERT INTO `sys_log` VALUES (3, '管理员 2', '127.0.0.1', '2023-05-16 20:53:20', '管理页登录, 登录状态：Login Success');
INSERT INTO `sys_log` VALUES (4, 'user01', '127.0.0.1', '2023-05-18 10:43:28', '管理页登录, 登录状态：Login Failed');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '公告栏id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标头',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告详情描述',
  `create_time` date NULL DEFAULT NULL COMMENT '公告创建时间',
  `end_time` date NULL DEFAULT NULL COMMENT '公告截止时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '618大拍卖', '618狂欢节就要到了！！本平台提供大量优惠活动快来抢购吧！！', '2023-05-30', '2023-06-19');

-- ----------------------------
-- Table structure for sys_order
-- ----------------------------
DROP TABLE IF EXISTS `sys_order`;
CREATE TABLE `sys_order`  (
  `order_id` int(30) NOT NULL AUTO_INCREMENT,
  `order_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int(20) NOT NULL,
  `product_id` int(20) NOT NULL,
  `bycount` int(10) NOT NULL DEFAULT 1,
  `state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '未支付',
  `deleted` int(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `order_code`(`order_code` ASC) USING BTREE,
  INDEX `f_prod_id`(`product_id` ASC) USING BTREE,
  INDEX `f_ur_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `f_prod_id` FOREIGN KEY (`product_id`) REFERENCES `sys_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `f_ur_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_order
-- ----------------------------
INSERT INTO `sys_order` VALUES (26, '1684724572088', 8, 1, 1, '已支付', 1);
INSERT INTO `sys_order` VALUES (27, '1684724572088', 8, 2, 1, '已支付', 1);
INSERT INTO `sys_order` VALUES (28, '1684725566935', 8, 1, 1, '未支付', 0);
INSERT INTO `sys_order` VALUES (29, '1684725566935', 8, 2, 1, '未支付', 0);
INSERT INTO `sys_order` VALUES (32, '1685959703189', 1, 1, 1, '已支付', 1);
INSERT INTO `sys_order` VALUES (33, '1685959703189', 1, 4, 1, '已支付', 1);
INSERT INTO `sys_order` VALUES (34, '1686013592571', 8, 1, 1, '未支付', 0);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路径',
  `orders` int(11) NULL DEFAULT 1 COMMENT '顺序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'grid' COMMENT '图标',
  `page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '页面路径',
  `auth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父级id',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型，1目录  2菜单 3按钮',
  `hide` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '首页', 'home', 2, 'house', 'Home', '', 0, 0, '2023-03-31 11:14:42', '2023-04-22 21:44:32', 1, 0);
INSERT INTO `sys_permission` VALUES (2, '系统管理', '', 2, 'setting', '', '', 0, 0, '2023-01-16 20:45:51', '2023-05-16 15:19:09', 2, 0);
INSERT INTO `sys_permission` VALUES (3, '用户管理', 'user', 1, 'star', 'User', 'user.list', 0, 0, '2023-01-16 20:45:51', '2023-05-16 15:31:19', 1, 0);
INSERT INTO `sys_permission` VALUES (6, '商品管理', '', 2, 'menu', '', '', 0, 0, '2023-04-09 18:00:22', '2023-04-09 18:00:25', 2, 0);
INSERT INTO `sys_permission` VALUES (7, '公告管理', 'notice', 1, 'promotion', 'Notice', 'notice.list', 0, 0, '2023-04-01 16:13:10', '2023-05-16 19:45:32', 1, 0);
INSERT INTO `sys_permission` VALUES (8, '数据字典管理', 'dict', 1, 'reading', 'Dict', 'dict.list', 0, 0, '2023-02-02 20:41:32', '2023-05-16 19:43:52', 1, 0);
INSERT INTO `sys_permission` VALUES (9, '登录日志', 'loginfo', 2, 'SwitchButton', 'LogInfo', 'log.list', 0, 0, '2023-05-16 00:13:55', '2023-05-16 19:42:13', 1, 0);
INSERT INTO `sys_permission` VALUES (21, '角色管理', 'role', 1, 'star', 'Role', 'role.list', 2, 0, '2023-01-31 20:32:59', '2023-05-16 15:45:24', 2, 0);
INSERT INTO `sys_permission` VALUES (22, '权限管理', 'permission', 1, 'editPen', 'Permission', 'permission.list', 2, 0, '2023-01-31 20:33:25', '2023-05-16 15:28:14', 2, 0);
INSERT INTO `sys_permission` VALUES (31, '批量删除', '', 1, 'grid', '', 'user.deleteBatch', 3, 0, '2023-02-02 22:32:22', '2023-02-02 22:32:22', 3, 0);
INSERT INTO `sys_permission` VALUES (32, '用户导入', '', 1, 'grid', '', 'user.import', 3, 0, '2023-02-02 22:32:53', '2023-02-02 22:32:53', 3, 0);
INSERT INTO `sys_permission` VALUES (33, '用户导出', '', 1, 'grid', '', 'user.export', 3, 0, '2023-02-02 22:33:08', '2023-02-02 22:33:08', 3, 0);
INSERT INTO `sys_permission` VALUES (35, '用户新增', '', 1, 'grid', '', 'user.add', 3, 0, '2023-04-01 16:10:38', '2023-04-01 16:10:40', 3, 0);
INSERT INTO `sys_permission` VALUES (36, '用户编辑', '', 1, 'grid', '', 'user.edit', 3, 0, '2023-04-01 16:11:44', '2023-04-01 16:11:48', 3, 0);
INSERT INTO `sys_permission` VALUES (37, '用户删除', '', 1, 'grid', '', 'user.delete', 3, 0, '2023-01-29 11:04:15', '2023-01-29 11:04:15', 3, 0);
INSERT INTO `sys_permission` VALUES (61, '商品列表', 'goods', 1, 'files', 'Goods', 'pro.list', 6, 0, '2023-04-22 22:06:46', '2023-05-16 19:49:14', 3, 0);
INSERT INTO `sys_permission` VALUES (62, '商品添加', '', 1, 'grid', '', 'pro.add', 6, 0, '2023-04-22 16:50:56', '2023-04-22 16:50:58', 3, 0);
INSERT INTO `sys_permission` VALUES (63, '商品删除', '', 1, 'grid', '', 'pro.delete', 6, 0, '2023-04-22 16:52:18', '2023-04-22 16:52:20', 3, 0);
INSERT INTO `sys_permission` VALUES (64, '商品编辑', '', 1, 'grid', '', 'pro.edit', 6, 0, '2023-04-23 15:33:09', '2023-04-23 15:33:11', 3, 0);
INSERT INTO `sys_permission` VALUES (65, '商品批量删除', '', 1, 'grid', '', 'pro.deleteBatch', 6, 0, '2023-04-22 16:53:01', '2023-04-22 16:53:03', 3, 0);
INSERT INTO `sys_permission` VALUES (66, '评论管理', 'comment', 1, 'ChatLineRound', 'Comment', 'comment.list', 6, 0, '2023-04-23 15:42:39', '2023-05-16 19:49:34', 3, 0);
INSERT INTO `sys_permission` VALUES (67, '评论删除', NULL, 1, 'grid', NULL, 'comment.delete', 6, 0, '2023-05-13 17:00:17', '2023-05-13 17:00:20', 3, 0);
INSERT INTO `sys_permission` VALUES (68, '评论批量删除', NULL, 1, 'grid', NULL, 'comment.deleteBatch', 6, 0, '2023-05-13 17:00:57', '2023-05-13 17:00:58', 3, 0);
INSERT INTO `sys_permission` VALUES (69, '商品种类', 'classify', 1, 'grid', 'Classify', 'classify.list', 6, 0, '2023-05-18 12:35:40', '2023-05-18 12:35:42', 3, 0);
INSERT INTO `sys_permission` VALUES (72, '公告新增', '', 1, 'grid', '', 'notice.add', 7, 0, '2023-04-01 16:12:33', '2023-04-01 16:12:49', 3, 0);
INSERT INTO `sys_permission` VALUES (75, '公告批量删除', '', 1, 'grid', '', 'notice.deleteBatch', 7, 0, '2023-04-01 16:12:42', '2023-04-01 16:13:03', 3, 0);
INSERT INTO `sys_permission` VALUES (76, '公告编辑', '', 1, 'grid', '', 'notice.edit', 7, 0, '2023-04-01 16:12:46', '2023-04-01 16:13:08', 3, 0);
INSERT INTO `sys_permission` VALUES (77, '公告删除', '', 1, 'grid', '', 'notice.delete', 7, 0, '2023-04-01 16:12:44', '2023-04-01 16:13:06', 3, 0);
INSERT INTO `sys_permission` VALUES (91, '日志下载', '', 1, 'grid', '', 'log.download', 9, 0, '2023-05-17 13:46:16', '2023-05-17 13:46:16', 3, 0);

-- ----------------------------
-- Table structure for sys_product
-- ----------------------------
DROP TABLE IF EXISTS `sys_product`;
CREATE TABLE `sys_product`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `product_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `classimg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pro_price` int(40) NULL DEFAULT NULL,
  `del_price` int(40) NULL DEFAULT NULL,
  `classid` int(11) NOT NULL,
  `stock` bigint(40) NOT NULL DEFAULT 1,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `classid`(`classid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_product
-- ----------------------------
INSERT INTO `sys_product` VALUES (1, '史记', '司马迁', '/image/shiji.png', 30, 28, 1, 2, 0);
INSERT INTO `sys_product` VALUES (2, '凡人修仙传', '忘语', '/image/xiuxianzhuan.png', 30, 29, 1, 1, 0);
INSERT INTO `sys_product` VALUES (3, '中国特色社会主义', '张维为', '/image/zhongte.png', 40, 30, 5, 1, 0);
INSERT INTO `sys_product` VALUES (4, '大河向东：他们为什么追随毛泽东', '陈冠任', '/image/dahexiangdong.png', 27, 17, 1, 0, 0);
INSERT INTO `sys_product` VALUES (5, '呐喊', '鲁迅', '/image/nahan.png', 30, 10, 1, 1, 0);
INSERT INTO `sys_product` VALUES (6, '彷徨', '鲁迅', '/image/panghuang.png', 22, 16, 1, 0, 0);
INSERT INTO `sys_product` VALUES (7, '三体', '刘慈欣', '/image/santi.png', 30, 20, 1, 1, 0);
INSERT INTO `sys_product` VALUES (8, 'Verilog数字系统设计教程', '夏宇闻', '/image/shuzixitongsheji.png', 33, 29, 1, 1, 0);
INSERT INTO `sys_product` VALUES (9, '模拟电子技术基础', '华成英', '/image/modianjiaoshishouce.png', 34, 28, 6, 1, 0);
INSERT INTO `sys_product` VALUES (10, '尚书', '先秦诸子', '/image/shangshu.png', 28, 12, 2, 1, 0);
INSERT INTO `sys_product` VALUES (11, '国史概要', '樊树志', '/image/guoshigaiyao.png', 26, 15, 1, 1, 0);
INSERT INTO `sys_product` VALUES (12, '神墓', '晨东', '/image/shenmu.png', 31, 25, 1, 1, 0);
INSERT INTO `sys_product` VALUES (13, '完美世界', '辰东', '/image/fantasy01.jpg', 31, 55, 4, 1, 0);
INSERT INTO `sys_product` VALUES (14, '凡人修仙传', '忘语', '/image/fantasy02.jpg', 57, 43, 4, 1, 1);
INSERT INTO `sys_product` VALUES (15, '斗罗大陆', '唐家三少', '/image/fantasy03.jpg', 24, 14, 4, 1, 0);
INSERT INTO `sys_product` VALUES (16, '遮天', '辰东', '/image/fantasy04.jpg', 76, 67, 4, 1, 0);
INSERT INTO `sys_product` VALUES (17, '斗破苍穹', '天蚕土豆', '/image/fantasy05.jpg', 788, 110, 4, 1, 0);
INSERT INTO `sys_product` VALUES (18, '雪中悍刀行', '烽火戏诸侯', '/image/fantasy06.jpg', 446, 316, 4, 1, 0);
INSERT INTO `sys_product` VALUES (19, '诛仙', '萧鼎', '/image/fantasy07.jpg', 682, 602, 4, 1, 0);
INSERT INTO `sys_product` VALUES (20, '悟空传', '今何在', '/image/fantasy08.jpg', 232, 85, 4, 1, 0);
INSERT INTO `sys_product` VALUES (21, '剑来', '烽火戏诸侯', '/image/fantasy09.jpg', 492, 459, 4, 1, 0);
INSERT INTO `sys_product` VALUES (22, '牧神记', '宅猪', '/image/fantasy10.jpg', 47, 410, 4, 1, 0);
INSERT INTO `sys_product` VALUES (24, '斗破苍穹', '天蚕土豆', '/image/fantasy12.jpg', 89, 998, 4, 1, 0);
INSERT INTO `sys_product` VALUES (25, '史记', '司马迁', '/image/history01.jpg', 95, 76, 2, 1, 0);
INSERT INTO `sys_product` VALUES (26, '南明史', '钱海岳', '/image/history02.jpg', 46, 343, 2, 1, 0);
INSERT INTO `sys_product` VALUES (27, '资治通鉴', '司马光', '/image/history03.jpg', 532, 136, 2, 1, 0);
INSERT INTO `sys_product` VALUES (28, '汉书', '班固', '/image/history04.jpg', 396, 311, 2, 1, 0);
INSERT INTO `sys_product` VALUES (29, '后汉书', '范晔', '/image/history05.jpg', 291, 532, 2, 1, 0);
INSERT INTO `sys_product` VALUES (30, '三国志', '陈寿', '/image/history06.jpg', 220, 273, 2, 1, 0);
INSERT INTO `sys_product` VALUES (31, '新五代史', '欧阳修', '/image/history07.jpg', 376, 835, 2, 1, 0);
INSERT INTO `sys_product` VALUES (32, 'Strawberry', 'Orakge', '/image/literary01.jpg', 26, 28, 5, 1, 0);
INSERT INTO `sys_product` VALUES (33, 'raspberry plus', 'arape', '/image/literary02.jpg', 26, 31, 5, 1, 0);
INSERT INTO `sys_product` VALUES (34, 'Grape', 'iGrape', '/image/literary03.jpg', 20, 31, 5, 1, 0);
INSERT INTO `sys_product` VALUES (35, 'Strawberry premium', 'Kbwi', '/image/literary04.jpg', 36, 21, 5, 1, 0);
INSERT INTO `sys_product` VALUES (36, 'ytrawberry premium', 'atrawberry', '/image/literary05.jpg', 33, 20, 5, 1, 0);
INSERT INTO `sys_product` VALUES (37, 'Rambutan', 'ultra-Strawberry', '/image/literary06.jpg', 22, 26, 5, 1, 0);
INSERT INTO `sys_product` VALUES (38, 'Mango', 'cpple', '/image/literary07.jpg', 35, 36, 5, 1, 0);
INSERT INTO `sys_product` VALUES (39, 'vluots pro', 'ultra-Rasmberry', '/image/literary08.jpg', 23, 21, 5, 1, 0);
INSERT INTO `sys_product` VALUES (40, 'Strawbekry', 'Cheroy pro', '/image/literary09.jpg', 36, 30, 5, 1, 0);
INSERT INTO `sys_product` VALUES (41, 'iPluots', 'ambi-fiwi', '/image/literary10.jpg', 40, 30, 5, 1, 0);
INSERT INTO `sys_product` VALUES (42, 'Grspe', 'Apple', '/image/literary11.jpg', 28, 38, 5, 1, 0);
INSERT INTO `sys_product` VALUES (43, 'ultra-Raspberry', 'Raspberry core', '/image/literary12.jpg', 34, 38, 5, 1, 0);
INSERT INTO `sys_product` VALUES (44, 'Strawberry', 'Orakge', '/image/science_fiction01.jpg', 26, 28, 7, 1, 0);
INSERT INTO `sys_product` VALUES (45, 'raspberry plus', 'arape', '/image/science_fiction02.jpg', 26, 31, 7, 1, 0);
INSERT INTO `sys_product` VALUES (46, 'Grape', 'iGrape', '/image/science_fiction03.jpg', 20, 31, 7, 1, 0);
INSERT INTO `sys_product` VALUES (47, 'Strawberry premium', 'Kbwi', '/image/science_fiction04.jpg', 36, 21, 7, 1, 0);
INSERT INTO `sys_product` VALUES (48, 'ytrawberry premium', 'atrawberry', '/image/science_fiction05.jpg', 33, 20, 7, 1, 0);
INSERT INTO `sys_product` VALUES (49, 'Rambutan', 'ultra-Strawberry', '/image/science_fiction06.jpg', 22, 26, 7, 1, 0);
INSERT INTO `sys_product` VALUES (50, 'Mango', 'cpple', '/image/science_fiction07.jpg', 35, 36, 7, 1, 0);
INSERT INTO `sys_product` VALUES (51, 'vluots pro', 'ultra-Rasmberry', '/image/science_fiction08.jpg', 23, 21, 7, 1, 0);
INSERT INTO `sys_product` VALUES (53, 'iPluots', 'ambi-fiwi', '/image/science_fiction10.jpg', 40, 30, 7, 1, 0);
INSERT INTO `sys_product` VALUES (54, 'Grspe', 'Apple', '/image/science_fiction11.jpg', 28, 38, 7, 1, 0);
INSERT INTO `sys_product` VALUES (55, 'ultra-Raspberry', 'Raspberry core', '/image/science_fiction12.jpg', 34, 38, 7, 1, 0);
INSERT INTO `sys_product` VALUES (56, 'Strawberry', 'Orakge', '/image/major01.jpg', 26, 28, 6, 1, 0);
INSERT INTO `sys_product` VALUES (57, 'raspberry plus', 'arape', '/image/major02.jpg', 26, 31, 6, 1, 0);
INSERT INTO `sys_product` VALUES (58, 'Grape', 'iGrape', '/image/major03.jpg', 20, 31, 6, 1, 0);
INSERT INTO `sys_product` VALUES (59, 'Strawberry premium', 'Kbwi', '/image/major04.jpg', 36, 21, 6, 1, 0);
INSERT INTO `sys_product` VALUES (60, 'ytrawberry premium', 'atrawberry', '/image/major05.jpg', 33, 20, 6, 1, 0);
INSERT INTO `sys_product` VALUES (61, 'Rambutan', 'ultra-Strawberry', '/image/major06.jpg', 22, 26, 6, 1, 0);
INSERT INTO `sys_product` VALUES (62, 'Mango', 'cpple', '/image/major07.jpg', 35, 36, 6, 1, 0);
INSERT INTO `sys_product` VALUES (63, 'vluots pro', 'ultra-Rasmberry', '/image/major08.jpg', 23, 21, 6, 1, 0);
INSERT INTO `sys_product` VALUES (64, 'Strawbekry', 'Cheroy pro', '/image/major09.jpg', 36, 30, 6, 1, 0);
INSERT INTO `sys_product` VALUES (65, 'iPluots', 'ambi-fiwi', '/image/major10.jpg', 40, 30, 6, 1, 0);
INSERT INTO `sys_product` VALUES (68, 'Strawberry', 'Orakge', '/image/partyhistory01.jpg', 26, 28, 3, 1, 0);
INSERT INTO `sys_product` VALUES (69, 'raspberry plus', 'arape', '/image/partyhistory02.jpg', 26, 31, 3, 1, 0);
INSERT INTO `sys_product` VALUES (70, 'Grape', 'iGrape', '/image/partyhistory03.jpg', 20, 31, 3, 1, 0);
INSERT INTO `sys_product` VALUES (71, 'Strawberry premium', 'Kbwi', '/image/partyhistory04.jpg', 36, 21, 3, 1, 0);
INSERT INTO `sys_product` VALUES (72, 'ytrawberry premium', 'atrawberry', '/image/partyhistory05.jpg', 33, 20, 3, 1, 0);
INSERT INTO `sys_product` VALUES (73, 'Rambutan', 'ultra-Strawberry', '/image/partyhistory06.jpg', 22, 26, 3, 1, 0);
INSERT INTO `sys_product` VALUES (74, 'Mango', 'cpple', '/image/partyhistory07.jpg', 35, 36, 3, 1, 0);
INSERT INTO `sys_product` VALUES (75, 'vluots pro', 'ultra-Rasmberry', '/image/partyhistory08.jpg', 23, 21, 3, 1, 0);
INSERT INTO `sys_product` VALUES (76, 'Strawbekry', 'Cheroy pro', '/image/partyhistory09.jpg', 36, 30, 3, 1, 0);
INSERT INTO `sys_product` VALUES (77, 'iPluots', 'ambi-fiwi', '/image/partyhistory10.jpg', 40, 30, 3, 1, 0);
INSERT INTO `sys_product` VALUES (78, 'Grspe', 'Apple', '/image/partyhistory11.jpg', 28, 38, 8, 1, 0);
INSERT INTO `sys_product` VALUES (79, 'ultra-Raspberry', 'Raspberry core', '/image/partyhistory12.jpg', 34, 38, 8, 1, 0);
INSERT INTO `sys_product` VALUES (90, 'A', 'A', '/image/bindex06_1684395466188-1686029138320.jpg', 20, 12, 1, 1, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `flag_deleted_idnex`(`flag` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ADMIN', 0, '2023-03-29 09:40:36', '2023-03-29 09:40:39');
INSERT INTO `sys_role` VALUES (2, '用户', 'USER', 0, '2023-03-29 09:41:10', '2023-03-29 09:41:13');
INSERT INTO `sys_role` VALUES (9, '访客', 'GUEST', 0, '2023-04-22 16:24:06', '2023-04-22 16:24:06');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_id`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (10, 1, 1);
INSERT INTO `sys_role_permission` VALUES (11, 1, 2);
INSERT INTO `sys_role_permission` VALUES (14, 1, 3);
INSERT INTO `sys_role_permission` VALUES (21, 1, 6);
INSERT INTO `sys_role_permission` VALUES (31, 1, 7);
INSERT INTO `sys_role_permission` VALUES (36, 1, 8);
INSERT INTO `sys_role_permission` VALUES (37, 1, 9);
INSERT INTO `sys_role_permission` VALUES (12, 1, 21);
INSERT INTO `sys_role_permission` VALUES (13, 1, 22);
INSERT INTO `sys_role_permission` VALUES (15, 1, 31);
INSERT INTO `sys_role_permission` VALUES (16, 1, 32);
INSERT INTO `sys_role_permission` VALUES (17, 1, 33);
INSERT INTO `sys_role_permission` VALUES (18, 1, 35);
INSERT INTO `sys_role_permission` VALUES (19, 1, 36);
INSERT INTO `sys_role_permission` VALUES (20, 1, 37);
INSERT INTO `sys_role_permission` VALUES (22, 1, 61);
INSERT INTO `sys_role_permission` VALUES (23, 1, 62);
INSERT INTO `sys_role_permission` VALUES (24, 1, 63);
INSERT INTO `sys_role_permission` VALUES (25, 1, 64);
INSERT INTO `sys_role_permission` VALUES (26, 1, 65);
INSERT INTO `sys_role_permission` VALUES (27, 1, 66);
INSERT INTO `sys_role_permission` VALUES (28, 1, 67);
INSERT INTO `sys_role_permission` VALUES (29, 1, 68);
INSERT INTO `sys_role_permission` VALUES (30, 1, 69);
INSERT INTO `sys_role_permission` VALUES (32, 1, 72);
INSERT INTO `sys_role_permission` VALUES (33, 1, 75);
INSERT INTO `sys_role_permission` VALUES (34, 1, 76);
INSERT INTO `sys_role_permission` VALUES (35, 1, 77);
INSERT INTO `sys_role_permission` VALUES (38, 1, 91);
INSERT INTO `sys_role_permission` VALUES (53, 2, 1);
INSERT INTO `sys_role_permission` VALUES (52, 2, 6);
INSERT INTO `sys_role_permission` VALUES (54, 2, 61);
INSERT INTO `sys_role_permission` VALUES (55, 2, 64);
INSERT INTO `sys_role_permission` VALUES (56, 2, 65);
INSERT INTO `sys_role_permission` VALUES (57, 2, 66);
INSERT INTO `sys_role_permission` VALUES (43, 9, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '74ce4a21f159e81638334cbe243cd2cf' COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `uid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户唯一id',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0存在  id删除',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '/avatars/header04.jpg' COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER' COMMENT '角色',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uid_index`(`uid` ASC) USING BTREE,
  UNIQUE INDEX `username_index`(`username` ASC, `deleted` ASC) USING BTREE,
  UNIQUE INDEX `email_index`(`email` ASC, `deleted` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin01', '00addc138e45d6c8275c7946141be5a3', '管理员 1', '780662364@qq.com', '北京市朝阳区三里屯路404号11室', '0fb4b43604d8209d95efbdfee2c9ba05', 0, '2023-03-29 09:32:04', '2023-06-05 11:39:30', '/avatars/header02.jpg', 'ADMIN', 'AAAAAAAAAAAAAAAAAAAAAA');
INSERT INTO `sys_user` VALUES (2, 'root01', '2726df7fbdfe872ab4c6c3088d178464', '管理员 2', '19827653421@qq.com', '东莞环区南街二巷635号27楼', '8100dd81f4cf95fe3223b988c9354777', 0, '2023-03-29 09:36:05', '2023-04-09 17:41:20', '/avatars/header02.jpg', 'ADMIN', 'BBBBBBBBBBBB');
INSERT INTO `sys_user` VALUES (3, 'aaa001', '1e87d05f0e029ae67e041d3e0307d635', '用户 1', '19854653421@qq.com', '成都市锦江区红星路三段999号25楼', 'be65cd2ea68324305c819edd96043d27', 0, '2023-03-29 00:00:00', '2023-04-22 16:48:32', '/avatars/header04.jpg', 'USER', 'CCCCCCCC');
INSERT INTO `sys_user` VALUES (4, 'bbb001', '607119ac1d39853ca02d9fbdbe1a8970', '用户 2', 'georgeh@gmail.com', '紫马岭商圈中山五路853号', 'IJ690i1n9E', 0, '2023-04-12 00:00:00', '2023-04-22 18:03:36', '/avatars/header05.png', 'GUEST', 'HBMHOWKIZB');
INSERT INTO `sys_user` VALUES (8, 'aitian01', '6024db4ce02f6c426f3af3390c13d2c0', 'aitian01', '1968215915@qq.com', '河南邓州市张楼乡茶庵村', 'e86152026fd944489c826405193cb532', 0, '2023-04-29 10:42:29', '2023-06-01 18:31:51', '/avatars/header04.jpg', 'USER', 'AITIAN001');
INSERT INTO `sys_user` VALUES (12, 'user01', '74ce4a21f159e81638334cbe243cd2cf', 'user01', '3213548648@qq.com', '', '9312806aa2c343e185829402bb7e7985', 0, '2023-05-18 10:42:43', '2023-05-18 10:42:43', '/avatars/header04.jpg', 'USER', NULL);

-- ----------------------------
-- Table structure for sys_user_news
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_news`;
CREATE TABLE `sys_user_news`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户消息表 id',
  `user_id` int(10) NOT NULL COMMENT '用户 id',
  `comment_id` int(10) NOT NULL COMMENT '评论 id',
  `received` int(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  PRIMARY KEY (`id`, `comment_id`) USING BTREE,
  INDEX `f_feiuserud`(`user_id` ASC) USING BTREE,
  INDEX `f_feicomid`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `f_feicomid` FOREIGN KEY (`comment_id`) REFERENCES `sys_comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `f_feiuserud` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_news
-- ----------------------------
INSERT INTO `sys_user_news` VALUES (1, 1, 2, 1);
INSERT INTO `sys_user_news` VALUES (2, 2, 5, 1);
INSERT INTO `sys_user_news` VALUES (3, 1, 10, 1);
INSERT INTO `sys_user_news` VALUES (4, 1, 12, 1);
INSERT INTO `sys_user_news` VALUES (5, 1, 18, 1);
INSERT INTO `sys_user_news` VALUES (6, 8, 20, 1);
INSERT INTO `sys_user_news` VALUES (7, 1, 22, 1);
INSERT INTO `sys_user_news` VALUES (8, 8, 23, 1);
INSERT INTO `sys_user_news` VALUES (9, 1, 24, 1);
INSERT INTO `sys_user_news` VALUES (11, 1, 25, 1);
INSERT INTO `sys_user_news` VALUES (12, 1, 26, 1);
INSERT INTO `sys_user_news` VALUES (13, 8, 27, 1);
INSERT INTO `sys_user_news` VALUES (14, 8, 28, 1);
INSERT INTO `sys_user_news` VALUES (15, 1, 29, 1);
INSERT INTO `sys_user_news` VALUES (16, 8, 30, 1);
INSERT INTO `sys_user_news` VALUES (17, 1, 31, 1);
INSERT INTO `sys_user_news` VALUES (18, 1, 32, 1);
INSERT INTO `sys_user_news` VALUES (19, 1, 33, 1);
INSERT INTO `sys_user_news` VALUES (20, 8, 34, 1);
INSERT INTO `sys_user_news` VALUES (21, 1, 35, 1);
INSERT INTO `sys_user_news` VALUES (22, 2, 36, 0);
INSERT INTO `sys_user_news` VALUES (23, 1, 37, 0);
INSERT INTO `sys_user_news` VALUES (24, 1, 38, 0);

-- ----------------------------
-- Table structure for sys_user_product
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_product`;
CREATE TABLE `sys_user_product`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '个人商品id',
  `user_id` int(20) NOT NULL,
  `product_id` int(50) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `f_user_id`(`user_id` ASC) USING BTREE,
  INDEX `f_product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `f_product_id` FOREIGN KEY (`product_id`) REFERENCES `sys_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `f_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_product
-- ----------------------------
INSERT INTO `sys_user_product` VALUES (1, 3, 16);
INSERT INTO `sys_user_product` VALUES (2, 4, 30);
INSERT INTO `sys_user_product` VALUES (3, 4, 22);
INSERT INTO `sys_user_product` VALUES (5, 1, 15);
INSERT INTO `sys_user_product` VALUES (6, 3, 72);
INSERT INTO `sys_user_product` VALUES (7, 12, 27);
INSERT INTO `sys_user_product` VALUES (8, 12, 24);
INSERT INTO `sys_user_product` VALUES (9, 1, 18);
INSERT INTO `sys_user_product` VALUES (10, 4, 43);
INSERT INTO `sys_user_product` VALUES (11, 2, 73);
INSERT INTO `sys_user_product` VALUES (12, 8, 20);
INSERT INTO `sys_user_product` VALUES (13, 4, 46);
INSERT INTO `sys_user_product` VALUES (14, 4, 78);
INSERT INTO `sys_user_product` VALUES (15, 2, 47);
INSERT INTO `sys_user_product` VALUES (16, 12, 3);
INSERT INTO `sys_user_product` VALUES (17, 1, 53);
INSERT INTO `sys_user_product` VALUES (18, 4, 69);
INSERT INTO `sys_user_product` VALUES (19, 1, 36);
INSERT INTO `sys_user_product` VALUES (20, 1, 39);
INSERT INTO `sys_user_product` VALUES (21, 4, 17);
INSERT INTO `sys_user_product` VALUES (22, 8, 44);
INSERT INTO `sys_user_product` VALUES (23, 1, 75);
INSERT INTO `sys_user_product` VALUES (24, 3, 10);
INSERT INTO `sys_user_product` VALUES (25, 12, 40);
INSERT INTO `sys_user_product` VALUES (26, 1, 33);
INSERT INTO `sys_user_product` VALUES (27, 2, 8);
INSERT INTO `sys_user_product` VALUES (28, 8, 61);
INSERT INTO `sys_user_product` VALUES (29, 1, 45);
INSERT INTO `sys_user_product` VALUES (30, 4, 7);
INSERT INTO `sys_user_product` VALUES (31, 3, 58);
INSERT INTO `sys_user_product` VALUES (32, 4, 77);
INSERT INTO `sys_user_product` VALUES (33, 1, 59);
INSERT INTO `sys_user_product` VALUES (34, 4, 13);
INSERT INTO `sys_user_product` VALUES (35, 12, 71);
INSERT INTO `sys_user_product` VALUES (36, 1, 38);
INSERT INTO `sys_user_product` VALUES (37, 8, 51);
INSERT INTO `sys_user_product` VALUES (38, 12, 60);
INSERT INTO `sys_user_product` VALUES (39, 4, 48);
INSERT INTO `sys_user_product` VALUES (40, 2, 34);
INSERT INTO `sys_user_product` VALUES (41, 2, 62);
INSERT INTO `sys_user_product` VALUES (42, 1, 54);
INSERT INTO `sys_user_product` VALUES (43, 8, 19);
INSERT INTO `sys_user_product` VALUES (44, 1, 74);
INSERT INTO `sys_user_product` VALUES (45, 2, 5);
INSERT INTO `sys_user_product` VALUES (46, 4, 41);
INSERT INTO `sys_user_product` VALUES (47, 4, 26);
INSERT INTO `sys_user_product` VALUES (48, 4, 4);
INSERT INTO `sys_user_product` VALUES (49, 8, 35);
INSERT INTO `sys_user_product` VALUES (50, 12, 21);
INSERT INTO `sys_user_product` VALUES (51, 2, 50);
INSERT INTO `sys_user_product` VALUES (52, 12, 1);
INSERT INTO `sys_user_product` VALUES (53, 2, 28);
INSERT INTO `sys_user_product` VALUES (54, 12, 56);
INSERT INTO `sys_user_product` VALUES (55, 2, 2);
INSERT INTO `sys_user_product` VALUES (56, 12, 42);
INSERT INTO `sys_user_product` VALUES (57, 2, 68);
INSERT INTO `sys_user_product` VALUES (58, 12, 63);
INSERT INTO `sys_user_product` VALUES (59, 1, 79);
INSERT INTO `sys_user_product` VALUES (60, 8, 6);
INSERT INTO `sys_user_product` VALUES (61, 3, 29);
INSERT INTO `sys_user_product` VALUES (62, 3, 57);
INSERT INTO `sys_user_product` VALUES (63, 1, 64);
INSERT INTO `sys_user_product` VALUES (64, 4, 9);
INSERT INTO `sys_user_product` VALUES (65, 3, 11);
INSERT INTO `sys_user_product` VALUES (66, 2, 12);
INSERT INTO `sys_user_product` VALUES (67, 8, 49);
INSERT INTO `sys_user_product` VALUES (68, 4, 55);
INSERT INTO `sys_user_product` VALUES (69, 4, 25);
INSERT INTO `sys_user_product` VALUES (70, 3, 31);
INSERT INTO `sys_user_product` VALUES (71, 8, 76);
INSERT INTO `sys_user_product` VALUES (72, 2, 37);
INSERT INTO `sys_user_product` VALUES (73, 2, 70);
INSERT INTO `sys_user_product` VALUES (74, 1, 65);
INSERT INTO `sys_user_product` VALUES (75, 8, 32);
INSERT INTO `sys_user_product` VALUES (76, 1, 90);
