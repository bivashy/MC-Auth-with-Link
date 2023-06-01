package me.mastercapexd.auth.crypto.belkaauth;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class UAuthUtil {
  private static SecureRandom RANDOM;
  
  public static String generateSalt() {
    byte[] salt = new byte[20];
    RANDOM.nextBytes(salt);
    return new String(bytesToString(salt));
  }
  
  public static byte[] getHash(String pass, byte[] salt, int iter, int len, String alg) throws Exception {
    PBEKeySpec ks = new PBEKeySpec(pass.toCharArray(), salt, iter, len);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(alg);
    return skf.generateSecret(ks).getEncoded();
  }
  
  public static String getHash(String pass, String salt, int iter, int len, String alg) throws Exception {
    byte[] hash = getHash(pass, stringToBytes(salt), iter, len, alg);
    return new String(bytesToString(hash));
  }
  
  public static char[] bytesToString(byte[] b) {
    char[] res = new char[b.length * 2];
    for (int i = 0; i < b.length; i++) {
      res[i * 2] = LOOKUP[b[i] >>> 4 & 0xF];
      res[i * 2 + 1] = LOOKUP[b[i] & 0xF];
    } 
    return res;
  }
  
  public static byte[] stringToBytes(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
      data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)); 
    return data;
  }
  
  public static String getHash(String salt, String password) {
    try {
      return getHash(password + "1_jm6H3tbLZ3DwZAqy8kVxAmjEJhl8ASypKUP-3d", salt, 50000, 160, "PBKDF2WithHmacSHA256");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private UAuthUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  static {
    try {
      RANDOM = SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  
  private static char[] LOOKUP = "0123456789abcdef".toCharArray();
}