-- 代驾服务后台管理系统测试数据

-- ============================================
-- 1. 插入测试用户数据
-- ============================================
INSERT INTO users (username, password, real_name, phone, email, status, avg_rating, total_ratings) VALUES
('user001', '123456', '张三', '13800138001', 'zhangsan@example.com', 1, 4.80, 12),
('user002', '123456', '李四', '13800138002', 'lisi@example.com', 1, 4.50, 8),
('user003', '123456', '王五', '13800138003', 'wangwu@example.com', 1, 4.90, 15),
('user004', '123456', '赵六', '13800138004', 'zhaoliu@example.com', 1, 4.20, 5),
('user005', '123456', '钱七', '13800138005', 'qianqi@example.com', 0, 3.50, 3);

-- ============================================
-- 2. 插入测试司机数据
-- ============================================
INSERT INTO drivers (username, password, real_name, phone, id_card, license_number, car_model, car_plate, status, avg_rating, total_ratings, dispatch_priority, service_permission) VALUES
('driver001', '123456', '刘师傅', '13900139001', '110101198001011234', 'A123456789', '大众迈腾', '京A12345', 1, 4.85, 120, 8, 1),
('driver002', '123456', '陈师傅', '13900139002', '110101198502022345', 'B987654321', '丰田凯美瑞', '京B54321', 1, 4.60, 85, 6, 1),
('driver003', '123456', '周师傅', '13900139003', '110101197803033456', 'C112233445', '别克君威', '京C67890', 1, 4.95, 200, 10, 1),
('driver004', '123456', '吴师傅', '13900139004', '110101199004044567', 'D556677889', '本田雅阁', '京D09876', 1, 4.30, 45, 4, 2),
('driver005', '123456', '郑师傅', '13900139005', '110101198205055678', 'E998877665', '日产天籁', '京E11223', 0, 3.80, 20, 2, 3);

-- ============================================
-- 3. 插入计费配置数据
-- ============================================
INSERT INTO pricing_configs (config_name, start_price, start_distance, per_km_price, free_wait_time, per_minute_wait_price, night_surcharge_start, night_surcharge_end, night_surcharge_rate, status, is_default) VALUES
('日间标准计费', 15.00, 3.00, 3.50, 10, 1.00, NULL, NULL, 1.00, 1, FALSE),
('夜间标准计费', 20.00, 3.00, 4.50, 10, 1.50, '22:00:00', '06:00:00', 1.30, 1, FALSE),
('高峰期计费', 18.00, 3.00, 4.00, 15, 1.20, NULL, NULL, 1.00, 1, FALSE),
('默认配置', 16.00, 3.00, 3.80, 10, 1.20, '22:00:00', '06:00:00', 1.25, 1, TRUE);

-- ============================================
-- 4. 插入支付方式配置数据
-- ============================================
INSERT INTO payment_configs (payment_name, payment_type, pay_mode, app_id, app_secret, merchant_id, api_key, notify_url, status) VALUES
('微信支付', 'WECHAT', 'BOTH', 'wx1234567890abcdef', 'wechat_secret_123456', '1234567890', 'wechat_api_key_123', 'https://api.example.com/payment/notify/wechat', 1),
('支付宝', 'ALIPAY', 'BOTH', '2021001234567890', 'alipay_private_key_123456', '2088123456789012', 'alipay_public_key_123', 'https://api.example.com/payment/notify/alipay', 1),
('银联支付', 'UNIONPAY', 'POSTPAY', 'unionpay_app_id_123', 'unionpay_secret_123', 'unionpay_merchant_123', 'unionpay_api_key_123', 'https://api.example.com/payment/notify/unionpay', 1);

-- ============================================
-- 5. 插入测试订单数据
-- ============================================
INSERT INTO orders (order_no, user_id, driver_id, start_address, end_address, distance, duration, wait_time, total_amount, payment_type, pay_mode, status, user_evaluated, driver_evaluated) VALUES
('DJ202401010001', 1, 1, '北京市朝阳区望京SOHO', '北京市海淀区中关村软件园', 15.50, 35, 5, 68.00, 'WECHAT', 'POSTPAY', 5, TRUE, TRUE),
('DJ202401010002', 2, 2, '北京市西城区西单商场', '北京市东城区王府井大街', 5.20, 18, 12, 32.50, 'ALIPAY', 'PREPAY', 5, TRUE, TRUE),
('DJ202401020003', 3, 3, '北京市丰台区北京西站', '北京市通州区新华大街', 25.80, 55, 8, 105.00, 'WECHAT', 'POSTPAY', 5, TRUE, TRUE),
('DJ202401020004', 1, 2, '北京市朝阳区三里屯', '北京市朝阳区国贸中心', 3.50, 12, 3, 18.00, 'ALIPAY', 'PREPAY', 5, FALSE, FALSE),
('DJ202401030005', 4, 1, '北京市海淀区北京大学', '北京市海淀区清华大学', 4.20, 15, 6, 22.00, 'WECHAT', 'POSTPAY', 3, FALSE, FALSE);

-- ============================================
-- 6. 插入用户评价记录数据（用户对司机的评价）
-- ============================================
INSERT INTO user_evaluations (order_id, user_id, driver_id, overall_rating, driving_skill, service_attitude, car_cleanliness, content, status) VALUES
(1, 1, 1, 5, 5, 5, 5, '刘师傅驾驶技术非常好，服务态度热情，车辆也很干净整洁，全程非常舒适，强烈推荐！', 1),
(2, 2, 2, 4, 4, 4, 4, '陈师傅整体不错，驾驶平稳，服务态度也可以，就是等待时间稍微长了一点，总体满意。', 1),
(3, 3, 3, 5, 5, 5, 5, '周师傅是我遇到过最好的代驾司机，技术一流，服务周到，车辆一尘不染，下次还会选择他！', 1);

-- ============================================
-- 7. 插入司机评价记录数据（司机对用户的评价）
-- ============================================
INSERT INTO driver_evaluations (order_id, driver_id, user_id, overall_rating, is_drunk, is_destination_changed, has_abnormal_behavior, content, status) VALUES
(1, 1, 1, 5, FALSE, FALSE, FALSE, '乘客非常礼貌，全程沟通愉快，没有特殊要求，是非常好的乘客。', 1),
(2, 2, 2, 4, TRUE, FALSE, FALSE, '乘客喝了一些酒，但行为举止正常，没有特殊要求，整体配合度很好。', 1),
(3, 3, 3, 5, FALSE, TRUE, FALSE, '乘客中途更改了目的地，但非常配合，额外支付了费用，是很好的乘客。', 1);
