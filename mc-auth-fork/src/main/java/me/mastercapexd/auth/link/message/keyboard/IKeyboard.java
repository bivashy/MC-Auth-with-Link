package me.mastercapexd.auth.link.message.keyboard;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

/**
 * @author Ubivashka 2 dimensional keyboard for vk telegram or something
 */
public interface IKeyboard extends Castable<IKeyboard> {
	/**
	 * Attach keyboard to message
	 * 
	 * @param message that will accept keyboard
	 */
	void attach(Message message);

	/**
	 * @return Is keyboard inline
	 */
	boolean isInline();

	/**
	 * Set inline state of keyboard
	 * 
	 * @param inline
	 */
	void setInline(boolean inline);

	/**
	 * @return 2 dimensional array of buttons
	 */
	Button[][] getButtons();

	void addButton(int row, Button button);

	public static interface IKeyboardBuilder extends Castable<IKeyboardBuilder> {
		IKeyboardBuilder button(int row, Button button);

		IKeyboardBuilder inline(boolean inline);

		IKeyboard build();
	}
}
