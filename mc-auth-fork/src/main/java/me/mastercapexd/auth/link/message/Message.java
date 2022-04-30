package me.mastercapexd.auth.link.message;

import java.io.File;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.user.LinkUser;

public interface Message extends Castable<Message> {
	/**
	 * Returns only message text.
	 * 
	 * @return message text.
	 */
	String getText();

	/**
	 * @return Keyboard that holds buttons
	 */
	IKeyboard getKeyboard();
	
	/**
	 * Upload photo to the message.
	 * 
	 * @param photo. Photo that need to upload
	 */
	void uploadPhoto(File photo);

	/**
	 * @param user User that will receive a message
	 */
	void send(LinkUser user);

	/**
	 * @param peerId id that will receive a message
	 */
	void send(Integer peerId);

	public static interface MessageBuilder extends Castable<MessageBuilder> {
		MessageBuilder keyboard(IKeyboard keyboard);
		
		MessageBuilder uploadPhoto(File photo);

		MessageBuilder text(String text);

		Message build();
	}
}
