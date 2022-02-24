package me.mastercapexd.auth.link.message.vk;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ubivashka.vk.callback.objects.VKMessage;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public class VKKeyboard implements IKeyboard<VKMessage, VKButton> {
	private static final Integer ROW_LIMIT = 5;

	private final List<List<VKButton>> buttons = new ArrayList<>();

	private boolean inline, removeAfterClick;

	private VKKeyboard() {
	}

	public static VKKeyboardBuilder newBuilder() {
		return new VKKeyboard().new VKKeyboardBuilder();
	}

	@Override
	public void attach(VKMessage message) {
		message.setKeyboard(buildKeyboard());
	}

	@Override
	public VKButton[][] getButtons() {
		return buttons.stream().map(list -> list.toArray(new VKButton[0])).toArray(VKButton[][]::new);
	}

	@Override
	public void addButton(int row, VKButton button) {
		if (row >= ROW_LIMIT)
			throw new IllegalArgumentException();
		while (buttons.size() <= row)
			buttons.add(new ArrayList<>());

		buttons.get(row).add(button);
	}

	public Keyboard buildKeyboard() {
		List<List<KeyboardButton>> buildedButtons = buttons.stream()
				.map(buttonList -> buttonList.stream().map(VKButton::getKeyboardButton).collect(Collectors.toList()))
				.collect(Collectors.toList());

		Keyboard keyboard = new Keyboard();
		keyboard.setInline(inline);
		keyboard.setOneTime(removeAfterClick);
		keyboard.setButtons(buildedButtons);
		return keyboard;
	}

	public class VKKeyboardBuilder {

		private VKKeyboardBuilder() {
		}

		public VKKeyboardBuilder setInline(boolean inline) {
			VKKeyboard.this.inline = inline;
			return this;
		}

		public VKKeyboardBuilder setRemoveAfterClick(boolean removeAfterClick) {
			VKKeyboard.this.removeAfterClick = removeAfterClick;
			return this;
		}

		public VKKeyboard getKeyboard() {
			return VKKeyboard.this;
		}

	}

}
