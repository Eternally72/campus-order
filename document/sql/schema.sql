-- ===================================
-- 校园二手交易平台数据库初始化脚本
-- ===================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE campus_order;

-- ===================================
-- 用户表
-- ===================================
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    gender TINYINT DEFAULT 0 COMMENT '性别（0-未知，1-男，2-女）',
    school VARCHAR(100) COMMENT '学校',
    student_id VARCHAR(50) COMMENT '学号',
    status TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-正常）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    UNIQUE KEY uk_username (username),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ===================================
-- 商品分类表
-- ===================================
CREATE TABLE IF NOT EXISTS t_category (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID（0表示顶级分类）',
    icon VARCHAR(255) COMMENT '分类图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ===================================
-- 商品表
-- ===================================
CREATE TABLE IF NOT EXISTS t_goods (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    title VARCHAR(200) NOT NULL COMMENT '商品标题',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    original_price DECIMAL(10,2) COMMENT '原价',
    images TEXT COMMENT '商品图片（JSON数组）',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    stock INT DEFAULT 1 COMMENT '库存数量',
    quality TINYINT DEFAULT 1 COMMENT '成色（1-全新，2-几乎全新，3-轻微使用痕迹，4-明显使用痕迹）',
    trade_type TINYINT DEFAULT 3 COMMENT '交易方式（1-线下交易，2-邮寄，3-都可以）',
    trade_location VARCHAR(200) COMMENT '交易地点',
    status TINYINT DEFAULT 1 COMMENT '状态（0-下架，1-在售，2-已售出）',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    favorite_count INT DEFAULT 0 COMMENT '收藏量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    INDEX idx_category_id (category_id),
    INDEX idx_seller_id (seller_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ===================================
-- 订单表
-- ===================================
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    buyer_id BIGINT NOT NULL COMMENT '买家ID',
    seller_id BIGINT NOT NULL COMMENT '卖家ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    goods_title VARCHAR(200) COMMENT '商品标题',
    goods_image VARCHAR(255) COMMENT '商品图片',
    goods_price DECIMAL(10,2) COMMENT '商品单价',
    quantity INT DEFAULT 1 COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态（0-待付款，1-待发货，2-待收货，3-已完成，4-已取消，5-已退款）',
    trade_type TINYINT COMMENT '交易方式（1-线下交易，2-邮寄）',
    trade_location VARCHAR(200) COMMENT '交易地点',
    address VARCHAR(500) COMMENT '收货地址',
    phone VARCHAR(20) COMMENT '联系电话',
    remark VARCHAR(500) COMMENT '买家留言',
    pay_time DATETIME COMMENT '支付时间',
    delivery_time DATETIME COMMENT '发货时间',
    finish_time DATETIME COMMENT '完成时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_buyer_id (buyer_id),
    INDEX idx_seller_id (seller_id),
    INDEX idx_goods_id (goods_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ===================================
-- 通知记录表
-- ===================================
CREATE TABLE IF NOT EXISTS t_notify_record (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type TINYINT NOT NULL COMMENT '通知类型（1-订单通知，2-系统通知，3-活动通知）',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    biz_id BIGINT COMMENT '关联业务ID',
    biz_type VARCHAR(50) COMMENT '业务类型',
    read_flag TINYINT DEFAULT 0 COMMENT '是否已读（0-未读，1-已读）',
    read_time DATETIME COMMENT '阅读时间',
    send_status TINYINT DEFAULT 0 COMMENT '发送状态（0-待发送，1-已发送，2-发送失败）',
    send_time DATETIME COMMENT '发送时间',
    fail_reason VARCHAR(500) COMMENT '失败原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_read_flag (read_flag),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知记录表';

-- ===================================
-- 用户收藏表
-- ===================================
CREATE TABLE IF NOT EXISTS t_favorite (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
    UNIQUE KEY uk_user_goods (user_id, goods_id),
    INDEX idx_user_id (user_id),
    INDEX idx_goods_id (goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- ===================================
-- 初始化分类数据
-- ===================================
INSERT INTO t_category (id, name, parent_id, sort, status) VALUES
(1, '数码产品', 0, 1, 1),
(2, '图书教材', 0, 2, 1),
(3, '生活用品', 0, 3, 1),
(4, '服饰鞋包', 0, 4, 1),
(5, '运动户外', 0, 5, 1),
(6, '其他', 0, 99, 1),
(101, '手机', 1, 1, 1),
(102, '电脑', 1, 2, 1),
(103, '平板', 1, 3, 1),
(104, '耳机', 1, 4, 1),
(201, '教材', 2, 1, 1),
(202, '考研资料', 2, 2, 1),
(203, '小说', 2, 3, 1);

