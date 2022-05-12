package me.mastercapexd.auth.link.message.vk;

import java.util.stream.Collectors;

import com.vk.api.sdk.objects.messages.Keyboard;

import me.mastercapexd.auth.link.message.keyboard.DefaultKeyboard;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

public class VKKeyboard extends DefaultKeyboard {
	public VKKeyboard() {
	}

	public VKKeyboard(Keyboard keyboard) {
		buttons = keyboard
				.getButtons().stream().map(buttonsList -> buttonsList.stream()
						.map(button -> new VKButton(button).as(Button.class)).collect(Collectors.toList()))
				.collect(Collectors.toList());
		keyboardType = (keyboard.getInline() != null && keyboard.getInline()) ? KeyboardType.inline()
				: KeyboardType.reply();
	}

	public Keyboard build() {
		Keyboard keyboard = new Keyboard();
		keyboard.setButtons(buttons.stream().map(listButtons -> listButtons.stream()
				.map(button -> button.as(VKButton.class).create()).collect(Collectors.toList()))
				.collect(Collectors.toList()));
		keyboard.setInline(keyboardType.isInline());
		return keyboard;
	}

	public class VKKeyboardBuilder extends DefaultKeyboardBuilder {
		public VKKeyboardBuilder() {
			super(VKKeyboard.this);
		}
	}
}
