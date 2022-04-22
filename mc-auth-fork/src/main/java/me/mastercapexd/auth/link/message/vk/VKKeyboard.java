package me.mastercapexd.auth.link.message.vk;

import java.util.List;
import java.util.stream.Collectors;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;

import me.mastercapexd.auth.link.message.keyboard.AbstractKeyboard;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

public class VKKeyboard extends AbstractKeyboard {
	private boolean removeAfterClick;

	private VKKeyboard() {
	}

	public VKKeyboard(Keyboard keyboard) {
		this.inline = keyboard.getInline() == null ? false : keyboard.getInline();
		this.removeAfterClick = keyboard.getOneTime() == null ? false : keyboard.getOneTime();
		this.buttons = keyboard.getButtons().stream().map(list -> list.stream()
				.map(keyboardButton -> new VKButton(keyboardButton).as(Button.class)).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public static VKKeyboardBuilder newBuilder() {
		return new VKKeyboard().new VKKeyboardBuilder();
	}

	public Keyboard buildKeyboard() {
		List<List<KeyboardButton>> buildedButtons = buttons.stream().map(buttonList -> {
			List<KeyboardButton> buttons = buttonList.stream().map(button -> button.as(VKButton.class))
					.map(VKButton::getKeyboardButton).collect(Collectors.toList());
			return buttons;
		}).collect(Collectors.toList());

		Keyboard keyboard = new Keyboard();
		keyboard.setInline(inline);
		keyboard.setOneTime(removeAfterClick);
		keyboard.setButtons(buildedButtons);
		return keyboard;
	}

	public class VKKeyboardBuilder implements IKeyboardBuilder {

		private VKKeyboardBuilder() {
		}

		@Override
		public IKeyboardBuilder button(int row, Button button) {
			VKKeyboard.this.addButton(row, button);
			return this;
		}

		@Override
		public IKeyboardBuilder inline(boolean inline) {
			VKKeyboard.this.inline = inline;
			return this;
		}

		@Override
		public VKKeyboard build() {
			return VKKeyboard.this;
		}

	}
}
