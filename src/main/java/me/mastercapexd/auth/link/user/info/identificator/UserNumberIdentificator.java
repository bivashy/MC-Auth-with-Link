package me.mastercapexd.auth.link.user.info.identificator;

public class UserNumberIdentificator implements LinkUserIdentificator {
	private long userId;
	private boolean isLong = false;

	public UserNumberIdentificator(long userId) {
		this.userId = userId;
		isLong = true;
	}

	public UserNumberIdentificator(int userId) {
		this.userId = userId;
	}

	@Override
	public long asNumber() {
		return userId;
	}

	@Override
	public String asString() {
		return Long.toString(userId);
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public LinkUserIdentificator setNumber(long userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public LinkUserIdentificator setString(String userId) {
		throw new UnsupportedOperationException("Cannot set identificator as string");
	}

	public boolean isLong() {
		return isLong;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!LinkUserIdentificator.class.isAssignableFrom(obj.getClass()))
			return false;
		LinkUserIdentificator other = (LinkUserIdentificator) obj;
		if (!other.isNumber())
			return false;
		return userId == other.asNumber();
	}
}
