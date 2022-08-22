package me.mastercapexd.auth;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.mindrot.jbcrypt.BCrypt;

import me.mastercapexd.auth.utils.HashUtils;

public enum HashType {
    MD5 {
        @Override
        public String hash(String string) {
            return HashUtils.hashMd5(string);
        }

        @Override
        public boolean checkHash(String string, String hash) {
            if (string == null || hash == null)
                return false;
            return hash(string).equals(hash);
        }
    }, SHA256 {
        @Override
        public String hash(String string) {
            return HashUtils.hashSha256(string);
        }

        @Override
        public boolean checkHash(String string, String hash) {
            if (string == null || hash == null)
                return false;
            return hash(string).equals(hash);
        }
    }, BCRYPT {
        @Override
        public String hash(String string) {
            return BCrypt.hashpw(string, BCrypt.gensalt());
        }

        @Override
        public boolean checkHash(String string, String hash) {
            return BCrypt.checkpw(string, hash);
        }
    };

    public abstract String hash(String string);

    public abstract boolean checkHash(String string, String hash);
}