package com.bivashy.auth.api.util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private HashUtils() {
    }

    private static final String FORMAT = "%032x";
    private static final String MD_5_EXTENSION = ".md5";
    private static MessageDigest MD5;
    private static MessageDigest SHA256;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
            SHA256 = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String hashText(String input, MessageDigest messageDigest) {
        return String.format(FORMAT, new BigInteger(1, messageDigest.digest(input.getBytes(StandardCharsets.UTF_8))));
    }

    public static URL mapToMd5URL(URL url) throws MalformedURLException {
        return new URL(url.toString() + MD_5_EXTENSION);
    }

    public static String getFileCheckSum(File file, MessageDigest messageDigest) throws IOException {
        if (!file.exists())
            return null;
        return new BigInteger(1, messageDigest.digest(Files.readAllBytes(file.toPath()))).toString(16);
    }

    public static MessageDigest getMD5() {
        return MD5;
    }

    public static MessageDigest getSHA256() {
        return SHA256;
    }
}
