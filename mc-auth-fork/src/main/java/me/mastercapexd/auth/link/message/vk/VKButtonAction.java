package me.mastercapexd.auth.link.message.vk;

import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction;

public class VKButtonAction implements ButtonAction {
	private final TemplateActionTypeNames buttonActionType;

	public VKButtonAction(TemplateActionTypeNames buttonActionType) {
		this.buttonActionType = buttonActionType;
	}

	public TemplateActionTypeNames getButtonActionType() {
		return buttonActionType;
	}

	public static class VKButtonActionBuilder implements ButtonActionBuilder {
		@Override
		public ButtonAction reply() {
			return new VKButtonAction(TemplateActionTypeNames.TEXT);
		}

		@Override
		public ButtonAction callback() {
			return new VKButtonAction(TemplateActionTypeNames.CALLBACK);
		}

		@Override
		public ButtonAction link() {
			return new VKButtonAction(TemplateActionTypeNames.OPEN_LINK);
		}
	}
}
