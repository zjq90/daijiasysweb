
package com.daijia.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    private static final Pattern ID_CARD_PATTERN_15 = Pattern.compile("^[1-9]\\d{5}\\d{2}((0[1-9])|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}[0-9Xx]$");
    
    private static final Pattern ID_CARD_PATTERN_18 = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$");
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private static final Pattern REAL_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]{2,20}$");
    
    private static final Pattern LICENSE_NUMBER_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,30}$");
    
    private static final Pattern LICENSE_TYPE_PATTERN = Pattern.compile("^[A-Za-z][0-9]?$");
    
    public static boolean isValidPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidIdCard(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        idCard = idCard.trim();
        if (idCard.length() == 15) {
            return ID_CARD_PATTERN_15.matcher(idCard).matches();
        } else if (idCard.length() == 18) {
            if (!ID_CARD_PATTERN_18.matcher(idCard).matches()) {
                return false;
            }
            return checkIdCard18(idCard);
        }
        return false;
    }
    
    private static boolean checkIdCard18(String idCard) {
        int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] parity = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard.charAt(i) - '0') * factor[i];
        }
        
        char checkCode = parity[sum % 11];
        char lastChar = Character.toUpperCase(idCard.charAt(17));
        
        return checkCode == lastChar;
    }
    
    public static boolean isValidUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    public static boolean isValidPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        int length = password.length();
        return length >= 6 && length <= 20;
    }
    
    public static boolean isValidRealName(String realName) {
        if (StringUtils.isBlank(realName)) {
            return false;
        }
        return REAL_NAME_PATTERN.matcher(realName).matches();
    }
    
    public static boolean isValidLicenseNumber(String licenseNumber) {
        if (StringUtils.isBlank(licenseNumber)) {
            return false;
        }
        return LICENSE_NUMBER_PATTERN.matcher(licenseNumber).matches();
    }
    
    public static boolean isValidLicenseType(String licenseType) {
        if (StringUtils.isBlank(licenseType)) {
            return false;
        }
        return LICENSE_TYPE_PATTERN.matcher(licenseType).matches();
    }
    
    public static boolean isValidCreditRating(BigDecimal creditRating) {
        if (creditRating == null) {
            return false;
        }
        double value = creditRating.doubleValue();
        return value >= 0 && value <= 5;
    }
    
    public static boolean isValidLatitude(BigDecimal latitude) {
        if (latitude == null) {
            return true;
        }
        double value = latitude.doubleValue();
        return value >= -90 && value <= 90;
    }
    
    public static boolean isValidLongitude(BigDecimal longitude) {
        if (longitude == null) {
            return true;
        }
        double value = longitude.doubleValue();
        return value >= -180 && value <= 180;
    }
    
    public static boolean isValidBalance(BigDecimal balance) {
        if (balance == null) {
            return false;
        }
        return balance.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    public static boolean isValidAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return false;
        }
        int length = address.trim().length();
        return length >= 2 && length <= 200;
    }
}
