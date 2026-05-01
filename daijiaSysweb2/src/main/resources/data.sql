-- 初始化信用等级数据
INSERT INTO credit_level (level_name, level_code, min_score, max_score, commission_rate, description) VALUES
('钻石级', 'A', 90, 100, 90.00, '最高等级，分成比例最高'),
('黄金级', 'B', 70, 89, 85.00, '优质司机，分成比例较高'),
('白银级', 'C', 50, 69, 80.00, '普通司机，标准分成比例'),
('青铜级', 'D', 0, 49, 75.00, '初级司机，分成比例较低');

-- 初始化代驾账户数据
INSERT INTO driver_account (driver_name, phone, id_card, balance, frozen_balance, total_income, total_withdraw, status) VALUES
('张三', '13800138001', '110101199001011001', 5680.50, 0.00, 15680.50, 10000.00, 'ACTIVE'),
('李四', '13800138002', '110101199001011002', 3250.80, 500.00, 8250.80, 5000.00, 'ACTIVE'),
('王五', '13800138003', '110101199001011003', 1200.00, 0.00, 6200.00, 5000.00, 'ACTIVE'),
('赵六', '13800138004', '110101199001011004', 800.00, 200.00, 3800.00, 3000.00, 'FROZEN'),
('钱七', '13800138005', '110101199001011005', 2500.00, 0.00, 12500.00, 10000.00, 'ACTIVE');

-- 初始化账户明细数据
INSERT INTO account_detail (account_id, order_no, type, amount, balance_before, balance_after, description) VALUES
(1, 'OD202604220001', 'INCOME', 150.00, 5530.50, 5680.50, '订单分成收入'),
(1, NULL, 'WITHDRAW', 5000.00, 10000.00, 5000.00, '账户提现'),
(2, 'OD202604220002', 'INCOME', 120.00, 3130.80, 3250.80, '订单分成收入'),
(2, NULL, 'PENALTY', 50.00, 3300.80, 3250.80, '投诉罚款'),
(3, 'OD202604220003', 'INCOME', 200.00, 1000.00, 1200.00, '订单分成收入'),
(5, 'OD202604220004', 'INCOME', 300.00, 2200.00, 2500.00, '订单分成收入');

-- 初始化订单分成配置数据
INSERT INTO commission_config (config_name, platform_rate, driver_rate, min_amount, max_amount, auto_settle, settle_delay_hours, status, description) VALUES
('标准分成配置', 15.00, 85.00, 0.00, 500.00, 1, 24, 'ACTIVE', '适用于普通订单的分成配置'),
('大额订单配置', 12.00, 88.00, 500.00, 999999.99, 1, 48, 'ACTIVE', '适用于500元以上的大额订单'),
('新手司机配置', 20.00, 80.00, 0.00, 999999.99, 0, 24, 'INACTIVE', '新手司机的分成配置，平台抽成较高');

-- 初始化订单数据
INSERT INTO order_info (order_no, driver_id, driver_name, passenger_name, passenger_phone, start_address, end_address, distance, duration, amount, status, complete_time) VALUES
('OD202604220001', 1, '张三', '乘客A', '13900139001', '北京市朝阳区国贸中心', '北京市海淀区中关村', 25.5, 45, 150.00, 'COMPLETED', '2026-04-22 20:30:00'),
('OD202604220002', 2, '李四', '乘客B', '13900139002', '北京市西城区西单', '北京市东城区王府井', 8.0, 20, 120.00, 'COMPLETED', '2026-04-22 19:45:00'),
('OD202604220003', 3, '王五', '乘客C', '13900139003', '北京市丰台区北京西站', '北京市通州区万达', 35.0, 60, 200.00, 'COMPLETED', '2026-04-22 21:00:00'),
('OD202604220004', 5, '钱七', '乘客D', '13900139004', '北京市大兴区机场', '北京市朝阳区三里屯', 45.0, 80, 300.00, 'COMPLETED', '2026-04-22 22:15:00'),
('OD202604220005', 1, '张三', '乘客E', '13900139005', '北京市海淀区五道口', '北京市朝阳区望京', 15.0, 30, 100.00, 'IN_PROGRESS', NULL),
('OD202604220006', NULL, NULL, '乘客F', '13900139006', '北京市东城区东直门', '北京市西城区金融街', 10.0, 25, 80.00, 'CREATED', NULL);

-- 初始化分成单数据
INSERT INTO commission_order (commission_no, order_no, driver_id, driver_name, config_id, order_amount, platform_commission, driver_commission, status, settle_time) VALUES
('CM202604220001', 'OD202604220001', 1, '张三', 1, 150.00, 22.50, 127.50, 'SETTLED', '2026-04-23 20:30:00'),
('CM202604220002', 'OD202604220002', 2, '李四', 1, 120.00, 18.00, 102.00, 'SETTLED', '2026-04-23 19:45:00'),
('CM202604220003', 'OD202604220003', 3, '王五', 1, 200.00, 30.00, 170.00, 'SETTLED', '2026-04-23 21:00:00'),
('CM202604220004', 'OD202604220004', 5, '钱七', 1, 300.00, 45.00, 255.00, 'PENDING', NULL);

-- 初始化投诉单数据
INSERT INTO complaint (complaint_no, order_no, driver_id, driver_name, complainant_name, complainant_phone, type, content, status, handle_remark, credit_deduct, handle_time) VALUES
('CP202604220001', 'OD202604220002', 2, '李四', '乘客B', '13900139002', '服务态度', '司机态度不好，说话不客气', 'RESOLVED', '已对司机进行批评教育，并扣除5分信用分', 5, '2026-04-23 10:00:00'),
('CP202604220002', NULL, 4, '赵六', '乘客G', '13900139007', '拒单', '司机接单后又取消订单', 'PENDING', NULL, 0, NULL),
('CP202604220003', 'OD202604220001', 1, '张三', '乘客A', '13900139001', '绕路', '司机故意绕路，多收费用', 'PROCESSING', '正在核实行车路线', 0, NULL);

-- 初始化对账单数据
INSERT INTO reconciliation (recon_no, driver_id, driver_name, start_date, end_date, total_orders, total_amount, total_commission, status, remark, confirm_time) VALUES
('RC20260401', 1, '张三', '2026-04-01', '2026-04-15', 15, 2250.00, 1912.50, 'CONFIRMED', '确认无误', '2026-04-16 15:00:00'),
('RC20260402', 2, '李四', '2026-04-01', '2026-04-15', 10, 1500.00, 1275.00, 'CONFIRMED', '确认无误', '2026-04-16 16:30:00'),
('RC20260403', 3, '王五', '2026-04-01', '2026-04-15', 8, 1200.00, 1020.00, 'DISPUTE', '有一笔订单分成有疑问', NULL),
('RC20260416', 1, '张三', '2026-04-16', '2026-04-22', 5, 750.00, 637.50, 'PENDING', NULL, NULL),
('RC20260416', 5, '钱七', '2026-04-16', '2026-04-22', 3, 450.00, 382.50, 'PENDING', NULL, NULL);
