package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor;

public class VKButtonColor implements ButtonColor {
	private final KeyboardButtonColor buttonColor;

	public VKButtonColor(KeyboardButtonColor buttonColor) {
		this.buttonColor = buttonColor;
	}

	public KeyboardButtonColor getButtonColor() {
		return buttonColor;
	}
	
	@Override
	public String toString() {
		return buttonColor.getValue();
	}

	public static class VKButtonColorBuilder implements ButtonColorBuilder {
		@Override
		public ButtonColor red() {
			return new VKButtonColor(KeyboardButtonColor.NEGATIVE);
		}

		@Override
		public ButtonColor blue() {
			return new VKButtonColor(KeyboardButtonColor.PRIMARY);
		}

		@Override
		public ButtonColor green() {
			return new VKButtonColor(KeyboardButtonColor.POSITIVE);
		}

		@Override
		public ButtonColor white() {
			return new VKButtonColor(KeyboardButtonColor.DEFAULT);
		}

		@Override
		public ButtonColor grey() {
			return new VKButtonColor(KeyboardButtonColor.DEFAULT);
		}
	}
}
