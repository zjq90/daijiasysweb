-- 代驾服务后台管理系统数据库表结构
-- 数据库：H2 Database

-- ============================================
-- 1. 用户表（存储乘客用户信息）
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    status INT DEFAULT 1 COMMENT '用户状态：1-正常，0-禁用',
    avg_rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '用户平均评分',
    total_ratings INT DEFAULT 0 COMMENT '评价次数',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 2. 司机表（存储司机信息）
-- ============================================
CREATE TABLE IF NOT EXISTS drivers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    id_card VARCHAR(18) NOT NULL UNIQUE,
    license_number VARCHAR(20) NOT NULL COMMENT '驾驶证号',
    car_model VARCHAR(50) COMMENT '车型',
    car_plate VARCHAR(20) COMMENT '车牌号',
    status INT DEFAULT 1 COMMENT '司机状态：1-正常，0-禁用，2-休息',
    avg_rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '司机平均评分',
    total_ratings INT DEFAULT 0 COMMENT '评价次数',
    dispatch_priority INT DEFAULT 5 COMMENT '派单优先级：1-10，越高越优先',
    service_permission INT DEFAULT 1 COMMENT '服务权限：1-全部服务，2-限制服务，3-暂停服务',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. 用户评价记录表（用户对司机的评价）
-- ============================================
CREATE TABLE IF NOT EXISTS user_evaluations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '评价用户ID',
    driver_id BIGINT NOT NULL COMMENT '被评价司机ID',
    overall_rating INT NOT NULL COMMENT '总体评分：1-5星',
    driving_skill INT COMMENT '驾驶技术评分：1-5星',
    service_attitude INT COMMENT '服务态度评分：1-5星',
    car_cleanliness INT COMMENT '车辆整洁评分：1-5星',
    content TEXT COMMENT '评价内容',
    status INT DEFAULT 1 COMMENT '评价状态：1-有效，0-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

-- ============================================
-- 4. 司机评价记录表（司机对用户的评价）
-- ============================================
CREATE TABLE IF NOT EXISTS driver_evaluations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    driver_id BIGINT NOT NULL COMMENT '评价司机ID',
    user_id BIGINT NOT NULL COMMENT '被评价用户ID',
    overall_rating INT NOT NULL COMMENT '总体评分：1-5星',
    is_drunk BOOLEAN DEFAULT FALSE COMMENT '是否醉酒',
    is_destination_changed BOOLEAN DEFAULT FALSE COMMENT '是否更改目的地',
    has_abnormal_behavior BOOLEAN DEFAULT FALSE COMMENT '是否有异常行为',
    content TEXT COMMENT '评价内容',
    status INT DEFAULT 1 COMMENT '评价状态：1-有效，0-已删除',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES drivers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================
-- 5. 计费设置表（系统计费配置）
-- ============================================
CREATE TABLE IF NOT EXISTS pricing_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    start_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '起步价',
    start_distance DECIMAL(10,2) NOT NULL DEFAULT 3.00 COMMENT '起步包含距离（公里）',
    per_km_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '每公里价格（超出起步距离后）',
    free_wait_time INT NOT NULL DEFAULT 10 COMMENT '免费等待时间（分钟）',
    per_minute_wait_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '超时每分钟等待费',
    night_surcharge_start TIME COMMENT '夜间加价开始时间',
    night_surcharge_end TIME COMMENT '夜间加价结束时间',
    night_surcharge_rate DECIMAL(3,2) DEFAULT 1.00 COMMENT '夜间加价倍率',
    status INT DEFAULT 1 COMMENT '配置状态：1-启用，0-禁用',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认配置',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 6. 支付方式配置表（支付参数配置）
-- ============================================
CREATE TABLE IF NOT EXISTS payment_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_name VARCHAR(50) NOT NULL COMMENT '支付方式名称',
    payment_type VARCHAR(20) NOT NULL COMMENT '支付类型：WECHAT-微信，ALIPAY-支付宝，UNIONPAY-银联',
    pay_mode VARCHAR(20) NOT NULL COMMENT '支付模式：PREPAY-预付，POSTPAY-后付，BOTH-两者支持',
    app_id VARCHAR(100) COMMENT '应用ID',
    app_secret VARCHAR(255) COMMENT '应用密钥',
    merchant_id VARCHAR(100) COMMENT '商户号',
    api_key VARCHAR(255) COMMENT 'API密钥',
    notify_url VARCHAR(255) COMMENT '回调通知地址',
    status INT DEFAULT 1 COMMENT '配置状态：1-启用，0-禁用',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 7. 订单表（存储订单信息）
-- ============================================
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    driver_id BIGINT COMMENT '司机ID',
    start_address VARCHAR(255) NOT NULL COMMENT '起点地址',
    end_address VARCHAR(255) NOT NULL COMMENT '终点地址',
    distance DECIMAL(10,2) COMMENT '行驶距离（公里）',
    duration INT COMMENT '行驶时长（分钟）',
    wait_time INT DEFAULT 0 COMMENT '等待时间（分钟）',
    total_amount DECIMAL(10,2) COMMENT '订单总金额',
    payment_type VARCHAR(20) COMMENT '支付方式',
    pay_mode VARCHAR(20) COMMENT '支付模式',
    status INT DEFAULT 0 COMMENT '订单状态：0-待接单，1-已接单，2-进行中，3-已完成，4-已取消，5-已支付',
    user_evaluated BOOLEAN DEFAULT FALSE COMMENT '用户是否已评价',
    driver_evaluated BOOLEAN DEFAULT FALSE COMMENT '司机是否已评价',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_evaluations_driver_id ON user_evaluations(driver_id);
CREATE INDEX IF NOT EXISTS idx_user_evaluations_user_id ON user_evaluations(user_id);
CREATE INDEX IF NOT EXISTS idx_driver_evaluations_user_id ON driver_evaluations(user_id);
CREATE INDEX IF NOT EXISTS idx_driver_evaluations_driver_id ON driver_evaluations(driver_id);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_driver_id ON orders(driver_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
