/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.analytics.util;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author aerol
 */
public final class AuthUtil {

    private AuthUtil() {
        throw new UnsupportedOperationException("Class new lenemez!");
    }

    public static String generateMd5Hash(String password) {
        String result = StringUtils.EMPTY;
        try {
            if (StringUtils.isNotEmpty(password)) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(password.getBytes("UTF-8"));
                byte[] digest = md.digest();
                result = DatatypeConverter.printHexBinary(digest).toUpperCase();
                result = result.toLowerCase();
            }
        } catch (Exception e) {
            result = "Md5Error";
        }
        return result;
    }

    public static boolean isUserPasswordValidate(String myPassword, String hashedPassword) {
        boolean result = false;
        try {
            if (StringUtils.isNotEmpty(myPassword) && StringUtils.isNotEmpty(hashedPassword)) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(myPassword.getBytes("UTF-8"));
                byte[] digest = md.digest();
                String myPasswordHash = DatatypeConverter
                        .printHexBinary(digest).toUpperCase();
                result = hashedPassword.toLowerCase().equals(myPasswordHash.toLowerCase());
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

}
