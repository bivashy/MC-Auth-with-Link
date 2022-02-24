package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.link.message.keyboard.button.AbstractButton;

public class VKButton extends AbstractButton {

	private KeyboardButton keyboardButton = new KeyboardButton();

	private KeyboardButtonAction action;
	private KeyboardButtonColor color;

	public VKButton(int row, int column, String label) {
		super(row, column, label);
	}

	public static ButtonBuilder newBuilder(int row, int column, String label) {
		return new VKButton(row, column, label).new ButtonBuilder();
	}

	public KeyboardButton getKeyboardButton() {
		keyboardButton.setAction(action);
		keyboardButton.setColor(color);
		return keyboardButton;
	}

	public KeyboardButtonAction getAction() {
		return action;
	}

	public KeyboardButtonColor getColor() {
		return color;
	}

	public class ButtonBuilder {
		private ButtonBuilder() {
		}

		public ButtonBuilder setColor(KeyboardButtonColor color) {
			VKButton.this.color = color;
			return this;
		}

		public ButtonBuilder setAction(KeyboardButtonAction action) {
			VKButton.this.action = action;
			return this;
		}
		
		public ButtonBuilder setCallbackPayload(String payload) {
			VKButton.this.action = new KeyboardButtonAction();
			action.setType(TemplateActionTypeNames.CALLBACK);
			action.setPayload(payload);
			return this;
		}

		public VKButton build() {
			return VKButton.this;
		}
	}

}
