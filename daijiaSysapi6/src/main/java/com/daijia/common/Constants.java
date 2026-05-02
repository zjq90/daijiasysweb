package com.daijia.common;

public class Constants {
    
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_ACCEPTED = "ACCEPTED";
    public static final String ORDER_STATUS_IN_SERVICE = "IN_SERVICE";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    
    public static final String ORDER_TYPE_IMMEDIATE = "IMMEDIATE";
    public static final String ORDER_TYPE_APPOINTMENT = "APPOINTMENT";
    
    public static final String DISPATCH_TYPE_ASSIGN = "ASSIGN";
    public static final String DISPATCH_TYPE_ROBBERY = "ROBBERY";
    
    public static final String PUSH_STATUS_PENDING = "PENDING";
    public static final String PUSH_STATUS_ACCEPTED = "ACCEPTED";
    public static final String PUSH_STATUS_IGNORED = "IGNORED";
    public static final String PUSH_STATUS_EXPIRED = "EXPIRED";
    
    public static final String PUSH_TYPE_ASSIGN = "ASSIGN";
    public static final String PUSH_TYPE_ROBBERY = "ROBBERY";
    
    public static final String PAY_STATUS_UNPAID = "UNPAID";
    public static final String PAY_STATUS_PAID = "PAID";
    public static final String PAY_STATUS_REFUNDED = "REFUNDED";
    
    public static final String EVALUATION_FROM_CLIENT = "CLIENT";
    public static final String EVALUATION_FROM_DRIVER = "DRIVER";
    
    public static final String COMPLAINT_STATUS_PENDING = "PENDING";
    public static final String COMPLAINT_STATUS_PROCESSING = "PROCESSING";
    public static final String COMPLAINT_STATUS_RESOLVED = "RESOLVED";
    public static final String COMPLAINT_STATUS_REJECTED = "REJECTED";
    
    public static final String EMERGENCY_STATUS_PENDING = "PENDING";
    public static final String EMERGENCY_STATUS_PROCESSING = "PROCESSING";
    public static final String EMERGENCY_STATUS_RESOLVED = "RESOLVED";
    
    public static final String RECORDING_STATUS_NOT_STARTED = "NOT_STARTED";
    public static final String RECORDING_STATUS_RECORDING = "RECORDING";
    public static final String RECORDING_STATUS_COMPLETED = "COMPLETED";
    
    public static final String PROMOTION_STATUS_ACTIVE = "ACTIVE";
    public static final String PROMOTION_STATUS_INACTIVE = "INACTIVE";
    public static final String PROMOTION_STATUS_EXPIRED = "EXPIRED";
    
    public static final String PROMOTION_TYPE_DISCOUNT = "DISCOUNT";
    public static final String PROMOTION_TYPE_CASHBACK = "CASHBACK";
    public static final String PROMOTION_TYPE_FULL_REDUCTION = "FULL_REDUCTION";
    
    public static final String TARGET_USER_ALL = "ALL";
    public static final String TARGET_USER_CLIENT = "CLIENT";
    public static final String TARGET_USER_DRIVER = "DRIVER";
    public static final String TARGET_USER_NEW = "NEW";
    
    public static final int ONLINE_STATUS_OFFLINE = 0;
    public static final int ONLINE_STATUS_ONLINE = 1;
    public static final int ONLINE_STATUS_BUSY = 2;
    
    public static final int STATUS_ENABLED = 1;
    public static final int STATUS_DISABLED = 0;
    
    public static final int VERIFIED_YES = 1;
    public static final int VERIFIED_NO = 0;
    
    public static final int DELETED_YES = 1;
    public static final int DELETED_NO = 0;
}
