CREATE DATABASE IF NOT EXISTS example_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE example_db;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status` TINYINT DEFAULT 0 COMMENT '订单状态 0-待付款 1-已付款 2-已发货 3-已完成 4-已取消',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '收货地址',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `receiver` VARCHAR(50) DEFAULT NULL COMMENT '收货人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '小计',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

INSERT INTO `sys_user` (`username`, `password`, `nickname`, `email`, `phone`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@example.com', '13800138000');

INSERT INTO `product_category` (`name`, `sort`) VALUES
('电子产品', 1),
('服装', 2),
('食品', 3);

INSERT INTO `product` (`name`, `description`, `price`, `stock`, `category_id`, `status`) VALUES
('iPhone 15', 'Apple iPhone 15 智能手机', 5999.00, 100, 1, 1),
('MacBook Pro', 'Apple MacBook Pro 14英寸', 14999.00, 50, 1, 1),
('AirPods Pro', 'Apple AirPods Pro 无线耳机', 1899.00, 200, 1, 1),
('男士T恤', '纯棉短袖T恤', 99.00, 500, 2, 1),
('运动跑鞋', '轻便透气跑鞋', 299.00, 300, 2, 1);
