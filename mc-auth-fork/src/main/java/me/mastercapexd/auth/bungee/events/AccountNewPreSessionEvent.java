package me.mastercapexd.auth.bungee.events;

import me.mastercapexd.auth.HashType;
import me.mastercapexd.auth.SessionResult;
import me.mastercapexd.auth.account.Account;

public class AccountNewPreSessionEvent extends AccountEvent {
	private final String enteredPasswordHash;
	private final HashType hashType;

	private SessionResult sessionResult = null;

	public AccountNewPreSessionEvent(Account account, HashType hashType, String enteredPasswordHash) {
		super(account);
		this.enteredPasswordHash = enteredPasswordHash;
		this.hashType = hashType;
	}

	public String getEnteredPasswordHash() {
		return enteredPasswordHash;
	}

	public HashType getHashType() {
		return hashType;
	}

	public SessionResult getSessionResult() {
		return sessionResult;
	}

	public void setSessionResult(SessionResult sessionResult) {
		this.sessionResult = sessionResult;
	}

}
