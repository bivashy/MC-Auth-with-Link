package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.KeyboardButtonColor;

import me.mastercapexd.auth.link.message.keyboard.button.Button;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor;

public class VKButtonColor implements ButtonColor {

	private final KeyboardButtonColor currentColor;

	public VKButtonColor(KeyboardButtonColor buttonColor) {
		this.currentColor = buttonColor;
	}

	@Override
	public void apply(Button button) {
		button.as(VKButton.class).setColor(currentColor);
	}
	
	@Override
	public String toText() {
		return currentColor.toString().toLowerCase();
	}

	public static class VKButtonColorBuilder implements ButtonColorBuilder {
		private static final VKButtonColorBuilder INSTANCE = new VKButtonColorBuilder();

		private VKButtonColorBuilder() {
		}

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

		public static VKButtonColorBuilder getInstance() {
			return INSTANCE;
		}

	}
}
