package me.mastercapexd.auth.vk;

public enum VKAccountsPageType {
	OWNPAGE("no-accounts", "accounts"), ALLACCOUNTSPAGE("admin-panel-no-accounts", "admin-panel-accounts"),
	ALLLINKEDACCOUNTSPAGE("admin-panel-no-linked-accounts", "admin-panel-linked-accounts");

	private final String noAccountsPath, accountsPath;

	private VKAccountsPageType(String noAccountsPath, String accountsPath) {
		this.noAccountsPath = noAccountsPath;
		this.accountsPath = accountsPath;
	}

	public String getNoAccountsPath() {
		return noAccountsPath;
	}

	public String getAccountsPath() {
		return accountsPath;
	}
}
