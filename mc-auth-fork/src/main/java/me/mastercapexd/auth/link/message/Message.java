package me.mastercapexd.auth.link.message;

import java.util.List;
import java.util.Map;

import me.mastercapexd.auth.link.message.keyboard.button.Button;
import me.mastercapexd.auth.link.user.LinkUser;

public interface Message<T extends LinkUser> {
	/**
	 * @return Raw content of message (Message content without button or any formatting)
	 */
	String getRawContent();
	
	/**
	 * @param rawContent that will be set as raw content of message.
	 */
	void setRawContent(String rawContent);
	
	/**
	 * @return buttons list if supported
	 */
	List<Button> getButtons();
	
	/**
	 * @param buttons that will be set as buttons of message.
	 */
	void setButtons(List<Button> buttons);
	
	/**
	 * @return Additional info of message. For example payload or id
	 */
	Map<String,String> getAdditionalInfo();
	
	/**
	 * @param user User that will receive a message
	 * @return Result of sending message
	 */
	LinkUserSendMessageResult sendMessage(T user);
}
