package me.mastercapexd.auth.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private HashUtils() {
    }

    private static final String FORMAT = "%032x";
    private static MessageDigest MD5;
    private static MessageDigest SHA256;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
            SHA256 = MessageDigest.getInstance("SHA256");
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String hashMd5(String input) {
        return String.format(FORMAT, new BigInteger(1, MD5.digest(input.getBytes(StandardCharsets.UTF_8))));
    }


    public static String hashSha256(String input) {
        return String.format(FORMAT, new BigInteger(1, SHA256.digest(input.getBytes(StandardCharsets.UTF_8))));
    }
}
