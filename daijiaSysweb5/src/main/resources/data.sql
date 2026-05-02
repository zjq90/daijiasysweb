-- =============================================
-- 代驾Web后台管理系统测试数据
-- =============================================

-- 管理员数据
INSERT INTO t_admin (username, password, real_name, phone, email, role, status) VALUES
('admin', '123456', '系统管理员', '13800138000', 'admin@daijia.com', 'super_admin', 1),
('manager', '123456', '运营经理', '13800138001', 'manager@daijia.com', 'admin', 1);

-- 用户数据（乘客）
INSERT INTO t_user (username, password, real_name, phone, email, avatar, status, balance) VALUES
('user001', '123456', '张三', '13900139001', 'zhangsan@example.com', '/images/default_avatar.png', 1, 500.00),
('user002', '123456', '李四', '13900139002', 'lisi@example.com', '/images/default_avatar.png', 1, 300.00),
('user003', '123456', '王五', '13900139003', 'wangwu@example.com', '/images/default_avatar.png', 1, 1000.00);

-- 司机数据
INSERT INTO t_driver (username, password, real_name, phone, id_card, driver_license, car_plate, car_model, car_color, avatar, status, online_status, latitude, longitude, rating, order_count) VALUES
('driver001', '123456', '刘师傅', '13700137001', '110101198001011234', 'A1234567890', '京A12345', '大众帕萨特', '黑色', '/images/default_driver.png', 1, 1, 39.9042000, 116.4074000, 4.80, 156),
('driver002', '123456', '王师傅', '13700137002', '110101198505055678', 'A0987654321', '京B67890', '丰田凯美瑞', '白色', '/images/default_driver.png', 1, 1, 39.9142000, 116.4174000, 4.90, 203),
('driver003', '123456', '陈师傅', '13700137003', '110101199010109012', 'A1122334455', '京C11111', '本田雅阁', '黑色', '/images/default_driver.png', 1, 0, 39.9242000, 116.4274000, 4.70, 89);

-- 订单数据
INSERT INTO t_order (order_no, user_id, driver_id, start_address, start_latitude, start_longitude, end_address, end_latitude, end_longitude, estimate_distance, estimate_time, estimate_amount, actual_distance, actual_time, actual_amount, status, order_time, accept_time, pickup_time, start_time, complete_time, rating, comment) VALUES
('DJ202401010001', 1, 1, '北京市朝阳区国贸大厦', 39.9088000, 116.4605000, '北京市海淀区中关村软件园', 40.0499000, 116.2851000, 28.50, 45, 158.00, 29.20, 48, 162.00, 5, '2024-01-01 18:30:00', '2024-01-01 18:32:00', '2024-01-01 18:40:00', '2024-01-01 18:45:00', '2024-01-01 19:33:00', 5, '司机服务很好，开车稳，推荐！'),
('DJ202401010002', 2, 2, '北京市西城区金融街', 39.9128000, 116.3634000, '北京市东城区王府井', 39.9139000, 116.4100000, 8.50, 20, 58.00, 9.20, 25, 62.00, 5, '2024-01-01 19:00:00', '2024-01-01 19:01:00', '2024-01-01 19:08:00', '2024-01-01 19:12:00', '2024-01-01 19:37:00', 4, '服务不错，就是稍微有点堵车'),
('DJ202401020001', 1, 1, '北京市海淀区五道口', 40.0030000, 116.3488000, '北京市朝阳区三里屯', 39.9339000, 116.4521000, 18.50, 35, 108.00, 19.00, 38, 112.00, 3, '2024-01-02 20:00:00', '2024-01-02 20:02:00', '2024-01-02 20:10:00', '2024-01-02 20:15:00', NULL, NULL, NULL),
('DJ202401020002', 3, NULL, '北京市丰台区北京西站', 39.8947000, 116.3225000, '北京市朝阳区望京SOHO', 40.0020000, 116.4700000, 25.00, 40, 138.00, NULL, NULL, NULL, 0, '2024-01-02 21:00:00', NULL, NULL, NULL, NULL, NULL, NULL);

-- 位置跟踪数据（针对进行中的订单 DJ202401020001）
INSERT INTO t_location_trace (order_id, driver_id, latitude, longitude, location_time, speed, direction) VALUES
(3, 1, 40.0030000, 116.3488000, '2024-01-02 20:15:00', 0, 0),
(3, 1, 40.0050000, 116.3508000, '2024-01-02 20:17:00', 35.5, 45),
(3, 1, 40.0080000, 116.3538000, '2024-01-02 20:19:00', 42.3, 60),
(3, 1, 40.0120000, 116.3578000, '2024-01-02 20:21:00', 38.7, 80),
(3, 1, 40.0180000, 116.3628000, '2024-01-02 20:23:00', 45.2, 90),
(3, 1, 40.0250000, 116.3688000, '2024-01-02 20:25:00', 40.0, 85),
(3, 1, 40.0320000, 116.3758000, '2024-01-02 20:27:00', 48.1, 75),
(3, 1, 40.0400000, 116.3858000, '2024-01-02 20:29:00', 50.5, 65);

-- 安全求助记录数据
INSERT INTO t_emergency_help (help_no, order_id, user_id, driver_id, help_type, latitude, longitude, address, content, emergency_contacts, status, create_time) VALUES
('EH202401010001', 1, 1, 1, 1, 39.9500000, 116.4000000, '北京市朝阳区建国路', '乘客感觉司机路线不对，触发紧急求助', '[{"name":"张小明","phone":"13900139010","relation":"儿子"},{"name":"李小红","phone":"13900139011","relation":"妻子"}]', 1, '2024-01-01 19:00:00'),
('EH202401010002', NULL, 2, NULL, 2, 39.9000000, 116.3500000, '北京市西城区西单', '测试求助', '[{"name":"李大明","phone":"13900139020","relation":"父亲"}]', 2, '2024-01-01 20:30:00');

-- 系统通知数据
INSERT INTO t_system_notice (title, content, notice_type, target_type, target_id, is_read, create_time) VALUES
('新订单提醒', '您有一个新的代驾订单，订单号：DJ202401020001，请及时处理', 1, 2, 1, 0, '2024-01-02 20:00:00'),
('紧急求助提醒', '订单 DJ202401010001 触发紧急求助，请立即处理！', 2, 3, 1, 0, '2024-01-01 19:00:00'),
('系统维护通知', '系统将于今晚22:00-23:00进行维护升级，请提前做好准备', 0, 0, NULL, 0, '2024-01-02 10:00:00');

-- 订单评价数据
INSERT INTO t_order_rating (order_id, user_id, driver_id, rating, comment, tags, anonymous, create_time) VALUES
(1, 1, 1, 5, '司机服务很好，开车稳，推荐！', '服务好,驾驶平稳,准时', 0, '2024-01-01 20:00:00'),
(2, 2, 2, 4, '服务不错，就是稍微有点堵车', '服务好', 1, '2024-01-01 21:00:00');
