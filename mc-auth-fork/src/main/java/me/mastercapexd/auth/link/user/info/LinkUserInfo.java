package me.mastercapexd.auth.link.user.info;

import java.util.Map;

public interface LinkUserInfo {

	/**
	 * @return unmodifable Map with additional info, for example messenger button
	 *         unique id.
	 */
	Map<String, String> getAdditionalInfos();

	/**
	 * @return link user id. For example messenger user id id, etc.
	 */
	Integer getLinkUserId();

	/**
	 * @param newLinkUserId new link user id. Need for relink to another account
	 *                      etc.
	 */
	void setLinkUserId(Integer newLinkUserId);

	/**
	 * @return returns is account have a enabled confirmation or not
	 */
	Boolean isConfirmationEnabled();

	/**
	 * @param confirmationEnabled that affect player.
	 */
	void setConfirmationEnabled(Boolean confirmationEnabled);
}
