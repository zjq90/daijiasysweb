-- =============================================
-- 代驾Web后台管理系统数据库表结构
-- 数据库：H2
-- =============================================

-- 1. 用户表（乘客/客户）
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1,
    balance DECIMAL(10,2) DEFAULT 0.00,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 司机表
CREATE TABLE IF NOT EXISTS t_driver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    id_card VARCHAR(18) NOT NULL UNIQUE,
    driver_license VARCHAR(50) NOT NULL,
    car_plate VARCHAR(20),
    car_model VARCHAR(100),
    car_color VARCHAR(50),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 0,
    online_status TINYINT DEFAULT 0,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    rating DECIMAL(3,2) DEFAULT 5.00,
    order_count INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 管理员表
CREATE TABLE IF NOT EXISTS t_admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(50) DEFAULT 'admin',
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    driver_id BIGINT,
    start_address VARCHAR(200) NOT NULL,
    start_latitude DECIMAL(10,7) NOT NULL,
    start_longitude DECIMAL(10,7) NOT NULL,
    end_address VARCHAR(200) NOT NULL,
    end_latitude DECIMAL(10,7) NOT NULL,
    end_longitude DECIMAL(10,7) NOT NULL,
    estimate_distance DECIMAL(10,2),
    estimate_time INT,
    estimate_amount DECIMAL(10,2),
    actual_distance DECIMAL(10,2),
    actual_time INT,
    actual_amount DECIMAL(10,2),
    status TINYINT DEFAULT 0,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accept_time TIMESTAMP,
    pickup_time TIMESTAMP,
    start_time TIMESTAMP,
    complete_time TIMESTAMP,
    cancel_time TIMESTAMP,
    cancel_reason VARCHAR(200),
    rating TINYINT,
    comment TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. 位置跟踪表（用于实时定位）
CREATE TABLE IF NOT EXISTS t_location_trace (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    location_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    speed DECIMAL(10,2),
    direction INT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. 安全求助记录表
CREATE TABLE IF NOT EXISTS t_emergency_help (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    help_no VARCHAR(32) NOT NULL UNIQUE,
    order_id BIGINT,
    user_id BIGINT,
    driver_id BIGINT,
    help_type TINYINT DEFAULT 0,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    address VARCHAR(200),
    content TEXT,
    audio_path VARCHAR(255),
    emergency_contacts TEXT,
    status TINYINT DEFAULT 0,
    admin_id BIGINT,
    handle_time TIMESTAMP,
    handle_result TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. 系统通知表
CREATE TABLE IF NOT EXISTS t_system_notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    notice_type TINYINT DEFAULT 0,
    target_type TINYINT DEFAULT 0,
    target_id BIGINT,
    is_read TINYINT DEFAULT 0,
    read_time TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. 录音记录表
CREATE TABLE IF NOT EXISTS t_audio_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(32) NOT NULL UNIQUE,
    order_id BIGINT,
    emergency_help_id BIGINT,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    duration INT,
    record_start_time TIMESTAMP,
    record_end_time TIMESTAMP,
    status TINYINT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. 订单评价表
CREATE TABLE IF NOT EXISTS t_order_rating (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    rating TINYINT NOT NULL,
    comment TEXT,
    tags VARCHAR(200),
    anonymous TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引以提高查询性能
CREATE INDEX idx_order_user_id ON t_order(user_id);
CREATE INDEX idx_order_driver_id ON t_order(driver_id);
CREATE INDEX idx_order_status ON t_order(status);
CREATE INDEX idx_order_order_time ON t_order(order_time);
CREATE INDEX idx_trace_order_id ON t_location_trace(order_id);
CREATE INDEX idx_trace_location_time ON t_location_trace(location_time);
CREATE INDEX idx_help_status ON t_emergency_help(status);
CREATE INDEX idx_help_create_time ON t_emergency_help(create_time);
CREATE INDEX idx_notice_target ON t_system_notice(target_type, target_id);
CREATE INDEX idx_notice_create_time ON t_system_notice(create_time);
