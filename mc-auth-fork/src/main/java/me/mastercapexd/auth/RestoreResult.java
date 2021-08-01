package me.mastercapexd.auth;

public enum RestoreResult {
	ACCOUNT_VK_NOT_EQUALS, RESTORED;

	private String passwordHash;

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
