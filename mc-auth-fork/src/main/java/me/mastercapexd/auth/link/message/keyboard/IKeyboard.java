package me.mastercapexd.auth.link.message.keyboard;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

/**
 * @author Ubivashka 2 dimensional keyboard for vk telegram or something
 */
public interface IKeyboard extends Castable<IKeyboard> {
	/**
	 * Returns copied 2d list of buttons
	 * 
	 * @return 2d list of buttons.
	 */
	List<List<Button>> getButtons();

	IKeyboard addRow(Button... buttons);

	IKeyboard removeIf(Predicate<Button> buttonFilter);

	IKeyboard ifThen(Predicate<Button> filter, Function<Button, Button> function);

	KeyboardType getType();
	
	public static interface IKeyboardBuilder {
		IKeyboardBuilder button(int row, Button button);

		IKeyboardBuilder buttons(List<List<Button>> buttons);

		IKeyboardBuilder row(Button... buttons);

		IKeyboardBuilder type(KeyboardType type);

		IKeyboard build();
	}
	
	public static interface KeyboardType extends Castable<KeyboardType> {
		default boolean isInline() {
			return false;
		}

		default boolean isReply() {
			return false;
		}

		static KeyboardType inline() {
			return new KeyboardType() {
				@Override
				public boolean isInline() {
					return true;
				}
			};
		}

		static KeyboardType reply() {
			return new KeyboardType() {
				@Override
				public boolean isReply() {
					return true;
				}
			};
		}
	}
}
