package me.mastercapexd.auth.link.message;

import java.util.Map;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.user.LinkUser;

public interface Message extends Castable<Message> {
	/**
	 * @return Raw content of message (Message content without button or any
	 *         formatting)
	 */
	String getRawContent();

	/**
	 * @param rawContent that will be set as raw content of message.
	 */
	void setRawContent(String rawContent);

	/**
	 * @return Keyboard that holds buttons
	 */
	IKeyboard getKeyboard();

	/**
	 * Changes message keyboard
	 * 
	 * @param keyboard that will be added to message
	 */
	void setKeyboard(IKeyboard keyboard);

	/**
	 * @return Additional info of message. For example payload or id
	 */
	Map<String, String> getAdditionalInfo();

	/**
	 * @param user User that will receive a message
	 * @return Result of sending message
	 */
	LinkUserSendMessageResult sendMessage(LinkUser user);

	/**
	 * @param peerId id that will receive a message
	 * @return Result of sending message
	 */
	LinkUserSendMessageResult sendMessage(Integer peerId);

	public static interface MessageBuilder extends Castable<MessageBuilder> {
		MessageBuilder keyboard(IKeyboard keyboard);

		MessageBuilder rawContent(String rawContent);

		Message build();
	}
}
