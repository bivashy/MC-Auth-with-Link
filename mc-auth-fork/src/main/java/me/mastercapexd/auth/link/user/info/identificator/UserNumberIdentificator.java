package me.mastercapexd.auth.link.user.info.identificator;

public class UserNumberIdentificator implements LinkUserIdentificator {
	private Integer userId;

	public UserNumberIdentificator(Integer userId) {
		this.userId = userId;
	}

	@Override
	public int asNumber() {
		return userId;
	}

	@Override
	public String asString() {
		return String.valueOf(userId);
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public LinkUserIdentificator setNumber(int userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public LinkUserIdentificator setString(String userId) {
		throw new UnsupportedOperationException("Cannot set identificator as string");
	}

}
