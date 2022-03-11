package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.link.message.keyboard.button.AbstractButton;

public class VKButton extends AbstractButton {

	private KeyboardButton keyboardButton = new KeyboardButton();

	private KeyboardButtonAction action = new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT);
	private KeyboardButtonColor color = KeyboardButtonColor.DEFAULT;

	public VKButton(String label) {
		super(label);
	}

	public static VKButtonBuilder newBuilder(String label) {
		return new VKButton(label).new VKButtonBuilder();
	}

	public KeyboardButton getKeyboardButton() {
		action.setLabel(label);
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

	public class VKButtonBuilder implements ButtonBuilder {
		private VKButtonBuilder() {
		}

		public VKButtonBuilder color(KeyboardButtonColor color) {
			VKButton.this.color = color;
			return this;
		}

		public VKButtonBuilder action(KeyboardButtonAction action) {
			VKButton.this.action = action;
			return this;
		}

		public VKButtonBuilder callbackPayload(String payload) {
			action.setType(TemplateActionTypeNames.CALLBACK);
			action.setPayload(payload);
			return this;
		}

		@Override
		public ButtonBuilder label(String label) {
			VKButton.this.label = label;
			return this;
		}

		@Override
		public VKButton build() {
			return VKButton.this;
		}

	}

}
