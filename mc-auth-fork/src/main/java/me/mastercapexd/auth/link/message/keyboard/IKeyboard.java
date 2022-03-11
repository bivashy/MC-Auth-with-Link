package me.mastercapexd.auth.link.message.keyboard;

import com.vk.api.sdk.objects.messages.Keyboard;

import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

/**
 * @author Ubivashka 2 dimensional keyboard for vk telegram or something
 */
public interface IKeyboard {
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

	/**
	 * Method that casts real keyboard object to the T
	 * 
	 * @param <T> Real type of keyboard for example: {@link Keyboard}
	 * @return Real keyboard that can be used for attaching keyboard or changing
	 *         custom values
	 */
	default <T extends IKeyboard> T as(Class<T> clazz) {
		return clazz.cast(this);
	}

	public static interface IKeyboardBuilder {
		IKeyboardBuilder button(int row, Button button);

		IKeyboardBuilder inline(boolean inline);

		IKeyboard build();

		default <T extends IKeyboardBuilder> T as(Class<T> clazz) {
			return clazz.cast(this);
		}
	}
}
