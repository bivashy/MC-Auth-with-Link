package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.link.message.keyboard.button.AbstractButton;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonColor;

public class VKButton extends AbstractButton {

	public static final String COLOR_INFO_KEY = "color";
	public static final String ACTION_INFO_KEY = "action";

	private KeyboardButton keyboardButton = new KeyboardButton();

	private KeyboardButtonAction action = new KeyboardButtonAction().setType(TemplateActionTypeNames.TEXT);
	private KeyboardButtonColor color = KeyboardButtonColor.DEFAULT;
	private String payload;

	private VKButton(String label) {
		super(label);
	}
	
	public VKButton(KeyboardButton keyboardButton) {
		super(keyboardButton.getAction().getLabel());
		this.action = keyboardButton.getAction();
		this.color = keyboardButton.getColor();
		this.payload = keyboardButton.getAction().getPayload();
	}

	public static VKButtonBuilder newBuilder(String label) {
		return new VKButton(label).new VKButtonBuilder();
	}

	public KeyboardButton getKeyboardButton() {
		if (payload != null)
			action.setPayload(payload);
		action.setLabel(label);
		keyboardButton.setAction(action);
		keyboardButton.setColor(color);
		return keyboardButton;
	}

	public KeyboardButtonAction getAction() {
		return action;
	}

	public void setAction(KeyboardButtonAction action) {
		this.action = action;
	}

	public KeyboardButtonColor getColor() {
		return color;
	}

	public void setColor(KeyboardButtonColor color) {
		this.color = color;
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
		public ButtonBuilder color(ButtonColor color) {
			color.apply(VKButton.this);
			return this;
		}

		@Override
		public ButtonBuilder action(ButtonAction action) {
			action.apply(VKButton.this);
			return this;
		}

		@Override
		public ButtonBuilder customId(String id) {
			VKButton.this.payload = id;
			return this;
		}

		@Override
		public VKButton build() {
			return VKButton.this;
		}

	}

}
