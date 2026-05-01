-- 代驾账户信息表
CREATE TABLE IF NOT EXISTS driver_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_name VARCHAR(100) NOT NULL COMMENT '司机姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    id_card VARCHAR(18) COMMENT '身份证号',
    balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    frozen_balance DECIMAL(10,2) DEFAULT 0.00 COMMENT '冻结余额',
    total_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计收入',
    total_withdraw DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计提现',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '账户状态：ACTIVE-正常，FROZEN-冻结，CANCELLED-注销',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 账户明细表
CREATE TABLE IF NOT EXISTS account_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL COMMENT '账户ID',
    order_no VARCHAR(50) COMMENT '关联订单号',
    type VARCHAR(20) NOT NULL COMMENT '明细类型：INCOME-收入，WITHDRAW-提现，REFUND-退款，PENALTY-罚款',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    balance_before DECIMAL(10,2) COMMENT '变动前余额',
    balance_after DECIMAL(10,2) COMMENT '变动后余额',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (account_id) REFERENCES driver_account(id)
);

-- 信用等级表
CREATE TABLE IF NOT EXISTS credit_level (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    level_code VARCHAR(20) NOT NULL COMMENT '等级代码：A、B、C、D',
    min_score INT NOT NULL COMMENT '最低分数',
    max_score INT NOT NULL COMMENT '最高分数',
    commission_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '分成比例（%）',
    description VARCHAR(200) COMMENT '等级描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 投诉单表
CREATE TABLE IF NOT EXISTS complaint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    complaint_no VARCHAR(50) NOT NULL COMMENT '投诉单号',
    order_no VARCHAR(50) COMMENT '关联订单号',
    driver_id BIGINT COMMENT '被投诉司机ID',
    driver_name VARCHAR(100) COMMENT '被投诉司机姓名',
    complainant_name VARCHAR(100) COMMENT '投诉人姓名',
    complainant_phone VARCHAR(20) COMMENT '投诉人电话',
    type VARCHAR(50) COMMENT '投诉类型：服务态度、迟到、拒单、绕路、其他',
    content TEXT COMMENT '投诉内容',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待处理，PROCESSING-处理中，RESOLVED-已解决，REJECTED-已驳回',
    handle_remark TEXT COMMENT '处理备注',
    credit_deduct INT DEFAULT 0 COMMENT '信用扣分',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    handle_time TIMESTAMP COMMENT '处理时间',
    FOREIGN KEY (driver_id) REFERENCES driver_account(id)
);

-- 订单分成配置表
CREATE TABLE IF NOT EXISTS commission_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    platform_rate DECIMAL(5,2) NOT NULL COMMENT '平台抽成比例（%）',
    driver_rate DECIMAL(5,2) NOT NULL COMMENT '司机分成比例（%）',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低订单金额',
    max_amount DECIMAL(10,2) DEFAULT 999999.99 COMMENT '最高订单金额',
    auto_settle TINYINT DEFAULT 0 COMMENT '是否自动清账：0-否，1-是',
    settle_delay_hours INT DEFAULT 24 COMMENT '自动清账延迟时间（小时）',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
    description VARCHAR(200) COMMENT '配置描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 分成单表
CREATE TABLE IF NOT EXISTS commission_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    commission_no VARCHAR(50) NOT NULL COMMENT '分成单号',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    driver_id BIGINT NOT NULL COMMENT '司机ID',
    driver_name VARCHAR(100) COMMENT '司机姓名',
    config_id BIGINT COMMENT '分成配置ID',
    order_amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    platform_commission DECIMAL(10,2) NOT NULL COMMENT '平台抽成金额',
    driver_commission DECIMAL(10,2) NOT NULL COMMENT '司机分成金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待结算，SETTLED-已结算，CANCELLED-已取消',
    settle_time TIMESTAMP COMMENT '结算时间',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (driver_id) REFERENCES driver_account(id),
    FOREIGN KEY (config_id) REFERENCES commission_config(id)
);

-- 对账单表
CREATE TABLE IF NOT EXISTS reconciliation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recon_no VARCHAR(50) NOT NULL COMMENT '对账单号',
    driver_id BIGINT NOT NULL COMMENT '司机ID',
    driver_name VARCHAR(100) COMMENT '司机姓名',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    total_orders INT DEFAULT 0 COMMENT '总订单数',
    total_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '总订单金额',
    total_commission DECIMAL(10,2) DEFAULT 0.00 COMMENT '总分成金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待确认，CONFIRMED-已确认，DISPUTE-有异议',
    remark VARCHAR(200) COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    confirm_time TIMESTAMP COMMENT '确认时间',
    FOREIGN KEY (driver_id) REFERENCES driver_account(id)
);

-- 订单表（用于测试）
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    driver_id BIGINT COMMENT '司机ID',
    driver_name VARCHAR(100) COMMENT '司机姓名',
    passenger_name VARCHAR(100) COMMENT '乘客姓名',
    passenger_phone VARCHAR(20) COMMENT '乘客电话',
    start_address VARCHAR(200) COMMENT '起点地址',
    end_address VARCHAR(200) COMMENT '终点地址',
    distance DECIMAL(10,2) COMMENT '行驶距离（公里）',
    duration INT COMMENT '行驶时长（分钟）',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status VARCHAR(20) DEFAULT 'CREATED' COMMENT '状态：CREATED-已创建，ACCEPTED-已接单，IN_PROGRESS-进行中，COMPLETED-已完成，CANCELLED-已取消',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    complete_time TIMESTAMP COMMENT '完成时间',
    FOREIGN KEY (driver_id) REFERENCES driver_account(id)
);
