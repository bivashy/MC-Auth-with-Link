package me.mastercapexd.auth.link.user.info.identificator;

import me.mastercapexd.auth.function.Castable;

public interface LinkUserIdentificator extends Castable<LinkUserIdentificator> {
	/**
	 * Returns identificator of user as number, may throw
	 * {@link UnsupportedOperationException} if it stores id as {@link String} or
	 * more complex data
	 * 
	 * @return Identificator as number
	 */
	int asNumber();

	/**
	 * Returns identificator as string, if id stores as number, it will convert
	 * number to string without throwing {@link UnsupportedOperationException},
	 * because it will use {@linkplain String#valueOf}
	 * 
	 * @return Identificator as string
	 */
	String asString();

	/**
	 * Returns result of casting object to number. May throw
	 * {@link NumberFormatException} if cannot format number.
	 * 
	 * @return Is identificator can be converted to number
	 */
	boolean isNumber();

	/**
	 * Set identificator as number, may throw {@link UnsupportedOperationException}
	 * if cannot set identificator as number
	 * 
	 * @param number. New identificator
	 * @return this {@link LinkUserIdentificator}
	 */
	LinkUserIdentificator setNumber(int userId);

	/**
	 * Set identificator as string, may throw {@link UnsupportedOperationException}
	 * if cannot set identificator as string
	 * 
	 * @param number. New identificator
	 * @return this {@link LinkUserIdentificator}
	 */
	LinkUserIdentificator setString(String userId);
}
