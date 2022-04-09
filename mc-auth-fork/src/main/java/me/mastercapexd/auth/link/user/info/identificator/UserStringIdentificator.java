package me.mastercapexd.auth.link.user.info.identificator;

public class UserStringIdentificator implements LinkUserIdentificator {
	private String userId;

	public UserStringIdentificator(String userId) {
		this.userId = userId;
	}

	@Override
	public int asNumber() {
		throw new UnsupportedOperationException("Cannot convert string to number");
	}

	@Override
	public String asString() {
		return userId;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public LinkUserIdentificator setNumber(int number) {
		throw new UnsupportedOperationException("Cannot set integer to string");
	}

	@Override
	public LinkUserIdentificator setString(String userId) {
		this.userId = userId;
		return this;
	}
}
