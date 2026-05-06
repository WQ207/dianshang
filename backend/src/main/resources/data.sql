INSERT INTO `sys_user` (`username`, `password`, `nickname`, `email`, `phone`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@example.com', '13800138000', 1),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'test@example.com', '13900139000', 1);

INSERT INTO `product_category` (`name`, `sort`, `status`) VALUES
('电子产品', 1, 1),
('服装', 2, 1),
('食品', 3, 1),
('图书', 4, 1);

INSERT INTO `product` (`name`, `description`, `price`, `stock`, `category_id`, `status`) VALUES
('iPhone 15', 'Apple iPhone 15 智能手机，搭载A17芯片', 5999.00, 100, 1, 1),
('MacBook Pro', 'Apple MacBook Pro 14英寸，M3 Pro芯片', 14999.00, 50, 1, 1),
('AirPods Pro', 'Apple AirPods Pro 2代无线耳机', 1899.00, 200, 1, 1),
('男士纯棉T恤', '100%纯棉面料，舒适透气', 99.00, 500, 2, 1),
('运动跑鞋', '轻便透气，缓震舒适', 299.00, 300, 2, 1),
('休闲牛仔裤', '经典版型，百搭时尚', 199.00, 400, 2, 1),
('进口巧克力', '比利时进口黑巧克力', 68.00, 150, 3, 1),
('坚果礼盒', '精选混合坚果，营养健康', 128.00, 80, 3, 1),
('Java编程思想', 'Java学习经典书籍', 89.00, 200, 4, 1),
('Vue3实战', 'Vue3框架实战指南', 79.00, 150, 4, 1);

INSERT INTO `orders` (`order_no`, `user_id`, `total_amount`, `status`, `address`, `phone`, `receiver`) VALUES
('ORD20240101001', 1, 7898.00, 3, '北京市朝阳区科技园区1号', '13800138001', '张三'),
('ORD20240101002', 1, 299.00, 1, '上海市浦东新区陆家嘴', '13800138002', '李四'),
('ORD20240101003', 2, 16998.00, 2, '广州市天河区珠江新城', '13900139001', '王五'),
('ORD20240101004', 2, 157.00, 0, '深圳市南山区科技园', '13900139002', '赵六'),
('ORD20240101005', 1, 128.00, 4, '杭州市西湖区', '13800138003', '钱七');

INSERT INTO `order_item` (`order_id`, `product_id`, `product_name`, `price`, `quantity`, `total_price`) VALUES
(1, 1, 'iPhone 15', 5999.00, 1, 5999.00),
(1, 3, 'AirPods Pro', 1899.00, 1, 1899.00),
(2, 5, '运动跑鞋', 299.00, 1, 299.00),
(3, 2, 'MacBook Pro', 14999.00, 1, 14999.00),
(3, 3, 'AirPods Pro', 1899.00, 1, 1899.00),
(4, 7, '进口巧克力', 68.00, 1, 68.00),
(4, 9, 'Java编程思想', 89.00, 1, 89.00),
(5, 8, '坚果礼盒', 128.00, 1, 128.00);
