package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.buttonshandler.VKCallbackButton;

public class ButtonFactory implements VKButtonFactory {

	@Override
	public VKCallbackButton createButton(String payload, VKButtonExecutor executor, List<String> aliases) {
		VKCallbackButton button = new VKCallbackButton(payload, executor);
		button.setAliases(new ArrayList<>());
		return button;
	}

}
