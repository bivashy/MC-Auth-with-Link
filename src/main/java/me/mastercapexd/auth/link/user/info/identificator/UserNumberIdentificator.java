package me.mastercapexd.auth.link.user.info.identificator;

public class UserNumberIdentificator implements LinkUserIdentificator {
	private long userId;

	public UserNumberIdentificator(long userId) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserNumberIdentificator other = (UserNumberIdentificator) obj;
		return userId == other.userId;
	}
}
