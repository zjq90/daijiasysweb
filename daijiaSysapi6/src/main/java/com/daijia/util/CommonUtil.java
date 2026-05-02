package com.daijia.util;

import cn.hutool.crypto.digest.DigestUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class CommonUtil {
    
    private static final DateTimeFormatter ORDER_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random RANDOM = new Random();
    
    public static String md5(String input) {
        return DigestUtil.md5Hex(input);
    }
    
    public static String generateOrderNo() {
        String datePart = LocalDateTime.now().format(ORDER_DATE_FORMATTER);
        String randomPart = String.format("%06d", RANDOM.nextInt(1000000));
        return "DJ" + datePart + randomPart;
    }
    
    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateVerifyCode() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
    
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
