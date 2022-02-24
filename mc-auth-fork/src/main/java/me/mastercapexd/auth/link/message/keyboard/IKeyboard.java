package me.mastercapexd.auth.link.message.keyboard;

/**
 * @author Ubivashka 2 dimensional keyboard for vk telegram or something
 */
public interface IKeyboard<T, E> {
	/**
	 * Attach keyboard to message
	 * 
	 * @param message that will accept keyboard
	 */
	void attach(T message);

	/**
	 * @return 2 dimensional array of buttons
	 */
	E[][] getButtons();

	void addButton(int row, E button);
}
