
package com.daijia.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    
    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }
    
    public static String md5WithSalt(String text, String salt) {
        return DigestUtils.md5Hex(text + salt);
    }
    
    public static boolean verify(String text, String md5) {
        return md5(text).equals(md5);
    }
    
    public static boolean verifyWithSalt(String text, String salt, String md5) {
        return md5WithSalt(text, salt).equals(md5);
    }
}
