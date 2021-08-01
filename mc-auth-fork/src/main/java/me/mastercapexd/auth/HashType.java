package me.mastercapexd.auth;

import org.mindrot.jbcrypt.BCrypt;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public enum HashType {

	MD5 {
		@Override
		public String hash(String string) {
			return Hashing.md5().newHasher().putString(string, Charsets.UTF_8).hash().toString();
		}
		
		@Override
		public boolean checkHash(String string, String hash) {
			if (string == null || hash == null)
				return false;
			return hash(string).equals(hash);
		}
	},
	SHA256 {
		@Override
		public String hash(String string) {
			return Hashing.sha256().newHasher().putString(string, Charsets.UTF_8).hash().toString();
		}
		
		@Override
		public boolean checkHash(String string, String hash) {
			if (string == null || hash == null)
				return false;
			return hash(string).equals(hash);
		}
	},
	BCRYPT {
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