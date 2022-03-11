package me.mastercapexd.auth.link.message.vk;

import com.google.gson.Gson;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;

import me.mastercapexd.auth.link.message.keyboard.button.Button;
import me.mastercapexd.auth.link.message.keyboard.button.ButtonAction;

public class VKButtonAction implements ButtonAction {

	private final KeyboardButtonAction currentAction;

	public VKButtonAction(KeyboardButtonAction buttonAction) {
		this.currentAction = buttonAction;
	}

	@Override
	public void apply(Button button) {
		button.as(VKButton.class).setAction(currentAction);
	}

	public static class VKButtonActionBuilder implements ButtonActionBuilder {

		private static final Gson GSON = new Gson();
		private static final VKButtonActionBuilder INSTANCE = new VKButtonActionBuilder();

		private VKButtonActionBuilder() {
		}

		@Override
		public ButtonAction callback(String id) {
			KeyboardButtonAction buttonAction = new KeyboardButtonAction();
			buttonAction.setPayload(GSON.toJson(id));
			buttonAction.setType(TemplateActionTypeNames.CALLBACK);
			return new VKButtonAction(buttonAction);
		}

		@Override
		public ButtonAction link(String url) {
			KeyboardButtonAction buttonAction = new KeyboardButtonAction();
			buttonAction.setLink(url);
			buttonAction.setType(TemplateActionTypeNames.OPEN_LINK);
			return new VKButtonAction(buttonAction);
		}

		public static VKButtonActionBuilder getInstance() {
			return INSTANCE;
		}

	}

}
